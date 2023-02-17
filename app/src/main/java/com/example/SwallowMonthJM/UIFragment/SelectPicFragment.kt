package com.example.SwallowMonthJM.UIFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.SwallowMonthJM.Adapter.SelectPicAdapter
import com.example.SwallowMonthJM.MainActivity
import com.example.SwallowMonthJM.R
import com.example.SwallowMonthJM.databinding.FragmentSelectPicBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.mozilla.javascript.tools.jsc.Main
import java.text.SimpleDateFormat


class SelectPicFragment(private val mainFrame : SlidingUpPanelLayout) : Fragment() {
    private var _binding : FragmentSelectPicBinding? = null
    private val binding  get() = _binding!!
    private lateinit var permissionListener : PermissionListener
    private lateinit var mainActivity:MainActivity

    private var lastUri :Uri? =null
    private var createUri :Uri?=null
    private var onItemClickListener : OnItemClickListener?= null


    private val startForResult = registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()) {}
    interface OnItemClickListener{
        fun onItemClick(lastUri:Uri?){}
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener = listener
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectPicBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.animation = mainActivity.aniList[1]
        view.animation.start()
        //권한 확인
        permissionListener = object : PermissionListener{
            override fun onPermissionGranted() {
                val adapter = SelectPicAdapter(mainActivity,getGallery())
                binding.imageRecycler.adapter = adapter
                    .apply {
                        setOnItemClickListener(object : SelectPicAdapter.OnItemClickListener{
                            override fun onItemClick(imageUri: Uri) {
                                if (imageUri.toString()==""){
                                    binding.selectImage.setImageResource(0)
                                    lastUri = imageUri
                                }else if (imageUri.toString()=="camera!"){
                                    //카메라 실행 코드
                                    openCamera()
                                }else{
                                    Glide.with(mainActivity)
                                        .load(imageUri)
                                        .into(binding.selectImage)
                                    lastUri = imageUri
                                }
                            }
                        })
                    }
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(mainActivity,"권한 필요", Toast.LENGTH_SHORT)
                    .show()
            }

        }


        //권한 적용
        TedPermission.with(mainActivity)
            .setPermissionListener(permissionListener)
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).check()

        setUpListener()
    }

    private fun setUpListener(){
        binding.cancelButton.setOnClickListener {
            mainFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }

        binding.uploadButton.setOnClickListener {
            mainFrame.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            onItemClickListener?.onItemClick(lastUri)
        }
    }

    @SuppressLint("Recycle")
    private fun getGallery() :ArrayList<Uri>{
        val picList = ArrayList<Uri>()
        picList.add(Uri.parse("")) //첫번째는 카메라
        val uriExternal : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var columnIndexId : Int
        val cursor = mainActivity.contentResolver.query(
            uriExternal,null,null,null,
            MediaStore.Images.ImageColumns.DATE_TAKEN+" DESC"
        )
        if (cursor!=null){
            while (cursor.moveToNext()){
                columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val imageId = cursor.getLong(columnIndexId)
                val uriImage = Uri.withAppendedPath(uriExternal,""+imageId)
                picList.add(uriImage)
            }
            cursor.close()
        }
        return picList
    }

    //카메라 촬영
    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName())?.let { uri->
            createUri = uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT,createUri)
            startForResult.launch(intent)
            createUri?.let {
                Glide.with(mainActivity)
                    .load(createUri)
                    .into(binding.selectImage)
                lastUri =createUri
            }
        }

    }

    //새 파일 이름 생성
    @SuppressLint("SimpleDateFormat")
    private fun newFileName() : String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmSS")
        return "${sdf.format(System.currentTimeMillis())}.jpeg"
    }

    //이미지 파일 생성 함수
    private fun createImageUri(filename : String) : Uri?{
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")

        return mainActivity.contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
    }

}