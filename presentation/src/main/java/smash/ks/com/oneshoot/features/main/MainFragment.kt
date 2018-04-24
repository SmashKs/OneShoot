/*
 * Copyright (C) 2018 The Smash Ks Open Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package smash.ks.com.oneshoot.features.main

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.ks.smash.ext.const.DEFAULT_INT
import kotlinx.android.synthetic.main.fragment_main.cv_camera
import kotlinx.android.synthetic.main.fragment_main.rv_fake
import kotlinx.android.synthetic.main.fragment_main.tv_label
import org.jetbrains.anko.bundleOf
import smash.ks.com.domain.objects.KsResponse
import smash.ks.com.oneshoot.R
import smash.ks.com.oneshoot.bases.AdvFragment
import smash.ks.com.oneshoot.bases.LoadView
import smash.ks.com.oneshoot.entities.KsEntity
import smash.ks.com.oneshoot.ext.aac.observe
import smash.ks.com.oneshoot.ext.aac.responseReaction
import smash.ks.com.oneshoot.ext.stubview.hideLoadingView
import smash.ks.com.oneshoot.ext.stubview.hideRetryView
import smash.ks.com.oneshoot.ext.stubview.showErrorView
import smash.ks.com.oneshoot.ext.stubview.showLoadingView
import smash.ks.com.oneshoot.ext.stubview.showRetryView
import smash.ks.com.oneshoot.widgets.recyclerview.MultiTypeAdapter

class MainFragment : AdvFragment<MainActivity, MainViewModel>(), LoadView {
    //region Instance
    companion object Factory {
        const val REQUEST_CAMERA_PERMISSION = 1
        // The key name of the fragment initialization parameters.
        const val ARG_RANDOM_ID = "param random image id"

        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @return A new instance of fragment [MainFragment].
         */
        fun newInstance(arg1: Int = DEFAULT_INT) = MainFragment().apply {
            arguments = bundleOf(ARG_RANDOM_ID to arg1)
        }
    }
    //endregion

    // The fragment initialization parameters.
    private val randomId by lazy { arguments?.getInt(ARG_RANDOM_ID) ?: DEFAULT_INT }

    override fun onResume() {
        super.onResume()
        // OPTIMIZE(jieyi): 2018/04/24 Extract an awesome method.
        when {
            ContextCompat.checkSelfPermission(parent,
                                              Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> cv_camera.start()
            ActivityCompat.shouldShowRequestPermissionRationale(parent,
                                                                Manifest.permission.CAMERA) -> ConfirmationDialogFragment
                .newInstance(R.string.camera_permission_confirmation,
                             arrayOf(Manifest.permission.CAMERA),
                             REQUEST_CAMERA_PERMISSION,
                             R.string.camera_permission_not_granted)
                .show(parent.supportFragmentManager, "Dialog")
            else -> ActivityCompat.requestPermissions(parent, arrayOf(Manifest.permission.CAMERA),
                                                      REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onPause() {
        super.onPause()
        cv_camera.stop()
    }

    //region Base Fragment
    override fun rendered(savedInstanceState: Bundle?) {
        observe(vm.temp, ::updateTemp)
        vm.retrieveId(randomId)

        rv_fake.apply {
            layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            adapter = MultiTypeAdapter(mutableListOf(KsEntity(), KsEntity()), context)
        }
    }

    override fun provideInflateView() = R.layout.fragment_main
    //endregion

    //region View Implementation for the Presenter.
    override fun showLoading() = parent.showLoadingView()

    override fun hideLoading() = parent.hideLoadingView()

    override fun showRetry() = parent.showRetryView()

    override fun hideRetry() = parent.hideRetryView()

    override fun showError(message: String) = parent.showErrorView(message)
    //endregion

    //region Presenter Implementation.
    private fun updateTemp(response: KsResponse?) {
        responseReaction(response) { showImageUri(it.data as String) }
    }

    private fun showImageUri(uri: String) {
        tv_label.text = uri
    }
    //endregion

    // OPTIMIZE(jieyi): 2018/04/24 I have the same dialog fragment.
    class ConfirmationDialogFragment : DialogFragment() {
        companion object {
            private const val ARG_MESSAGE = "message"
            private const val ARG_PERMISSIONS = "permissions"
            private const val ARG_REQUEST_CODE = "request_code"
            private const val ARG_NOT_GRANTED_MESSAGE = "not_granted_message"

            fun newInstance(
                @StringRes message: Int,
                permissions: Array<String>,
                requestCode: Int,
                @StringRes notGrantedMessage: Int
            ) = ConfirmationDialogFragment().apply {
                arguments = bundleOf(ARG_MESSAGE to message,
                                     ARG_PERMISSIONS to permissions,
                                     ARG_REQUEST_CODE to requestCode,
                                     ARG_NOT_GRANTED_MESSAGE to notGrantedMessage)
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
            val args = arguments as Bundle
            return AlertDialog.Builder(activity)
                .setMessage(args.getInt(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok,
                                   { dialog, which ->
                                       val permissions =
                                           args.getStringArray(ARG_PERMISSIONS) ?: throw IllegalArgumentException()
                                       ActivityCompat.requestPermissions(activity!!,
                                                                         permissions, args.getInt(ARG_REQUEST_CODE))
                                   })
                .setNegativeButton(android.R.string.cancel,
                                   { dialog, which ->
                                       Toast.makeText(activity,
                                                      args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                                      Toast.LENGTH_SHORT).show()
                                   })
                .create()
        }
    }
}