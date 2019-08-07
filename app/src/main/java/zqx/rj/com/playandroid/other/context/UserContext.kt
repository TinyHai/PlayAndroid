package zqx.rj.com.playandroid.other.context

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.zhan.mvvm.common.Preference
import zqx.rj.com.mvvm.state.UserState
import zqx.rj.com.playandroid.other.context.callback.collect.CollectListener
import zqx.rj.com.playandroid.other.context.callback.login.LoginSucState
import zqx.rj.com.playandroid.R
import zqx.rj.com.playandroid.other.constant.Const
import zqx.rj.com.playandroid.other.constant.Key

/**
 * author：  HyZhan
 * created： 2018/10/18 9:56
 * desc：    用户状态管理(登录/未登录) -> (状态设计模式)
 */
object UserContext{

    private var isLogin: Boolean by Preference(Key.LOGIN, false)
    // 委托属性   将实现委托给了 -> Preference
    var username by Preference(Key.USERNAME, Const.NOT_LOGIN_MSG)

    // 设置默认状态
    var mState: UserState = if (isLogin) LoginState() else LogoutState()

    // 收藏
    fun collect(context: Context?, position: Int, listener: CollectListener) {
        mState.collect(context, position, listener)
    }

    fun goCollectActivity(context: Context?) {
        mState.goCollectActivity(context)
    }

    // 跳转到todo
    fun goTodoActivity(context: Context?) {
        mState.goTodoActivity(context)
    }

    // 跳转去登录
    fun login(context: Activity?) {
        mState.login(context)
    }

    fun logout(activity: Activity){

        if (!isLogin){
            // TODO toast 请先登录
            return
        }


        AlertDialog.Builder(activity)
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_tips)
            .setPositiveButton(R.string.cancel, null)
            .setNegativeButton(R.string.right) { _, _ ->
                // 清除数据
                clearLoginData()
            }.show()
    }

    fun loginSuccess(username: String, collectIds: List<Int>?) {
        // 改变 sharedPreferences   isLogin值
        isLogin = true
        mState = LoginState()

        // 登录成功 回调 -> DrawerLayout -> 个人信息更新状态
        LoginSucState.notifyLoginState(username, collectIds)
    }

    fun clearLoginData() {
        mState = LogoutState()

        // 清除 cookie、登录缓存
        Preference.clear()

        LoginSucState.notifyLoginState(Const.NOT_LOGIN_MSG, null)
    }
}