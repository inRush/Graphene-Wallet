package com.gxb.gxswallet.page.firstin;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gxb.gxswallet.App;
import com.gxb.gxswallet.R;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.sxds.common.widget.AnimationBottomSheetDialog;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出框
 *
 * @author inrush
 */
public class PermissionsFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {

    /**
     * 权限回调标识
     */
    private static final int RC = 0x8888;

    private QMUIRoundButton mSubmit;

    private onPermissionGrantedListener mListener;

    public interface onPermissionGrantedListener {
        void onGranted(List<String> perms);
    }

    public void setOnPermissionGrantedListener(onPermissionGrantedListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AnimationBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 获取根布局
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        mSubmit = root.findViewById(R.id.btn_submit);

        mSubmit.setOnClickListener(
                v -> {
                    // 点击按钮时申请权限
                    requestPerm();
                    mSubmit.setText(getString(R.string.label_permission_process));
                }
        );
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 界面显示的时候进行刷新
        refreshState(getView());
    }

    /**
     * 刷新布局中的图片的状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (root == null) {
            return;
        }
        Context context = getContext();

        boolean haveRead = haveReadPerm(context);
        boolean haveWrite = haveWritePerm(context);
        boolean haveCamera = haveCameraPerm(context);

        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveRead ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write)
                .setVisibility(haveWrite ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_camera)
                .setVisibility(haveCamera ? View.VISIBLE : View.GONE);

        if (haveRead && haveWrite && haveCamera) {
            mSubmit.setText(getString(R.string.label_permission_complete));
            mSubmit.setClickable(false);
        } else {
            mSubmit.setText(getString(R.string.label_permission_submit));
        }
    }


    /**
     * 获取是否有外部存储读取权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveReadPerm(Context context) {
        // 准备需要检查的读取权限
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有外部存储写入权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveWritePerm(Context context) {
        // 准备需要检查的写入权限
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 获取是否有录音权限
     *
     * @param context 上下文
     * @return true就是有
     */
    private static boolean haveCameraPerm(Context context) {
        // 准备需要检查的录音权限
        String[] perms = new String[]{
                Manifest.permission.CAMERA
        };

        return EasyPermissions.hasPermissions(context, perms);
    }


    /**
     * show方法
     *
     * @param manager FragmentManager
     */
    public static void show(FragmentManager manager, onPermissionGrantedListener listener) {
        // 显示BottomSheetDialog
        PermissionsFragment fragment = new PermissionsFragment();
        fragment.setOnPermissionGrantedListener(listener);
        fragment.show(manager, PermissionsFragment.class.getName());
    }


    /**
     * 检查是否具有所有的权限
     *
     * @param context 上下文
     * @return 是否有所有的权限
     */
    public static boolean havAllPermissions(Context context) {
        return haveReadPerm(context) &&
                haveWritePerm(context) &&
                haveCameraPerm(context);
    }

    /**
     * 申请权限
     */
    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            App.showToast(R.string.label_permission_ok);
            // Fragment 中调用getView获取根布局,前提是正在onCreateView之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),
                    RC, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mListener.onGranted(perms);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果有没有申请成功的权限存在,则弹出弹出框,用户点击去到设置界面自己打开权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build().show();
        }

    }


    /**
     * 权限申请回调的方法
     * 在这个方法中把对应的申请状态交给EasyPermissions框架
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 传递对应的参数,并且告知接受权限处理者是this
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
