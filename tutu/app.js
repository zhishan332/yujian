//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        console.log('logincode:' + res.code);
        if (res.code) {
          //发起网络请求
          wx.request({
            url: 'http://127.0.0.1:8080/api/user/onLogin',
            method:'POST',
            data: {
              code: res.code
            },
            success: function (res) {
              var data = res.data;
              if (data.status == 1) {
                var openid = res.data.data.openid
                var session_key = res.data.data.session_key
                this.globalData.userInfo = openid
                console.log("登录成功" + openid)
              } else {
                console.log('登录失败' + res.data.msg)
              }
            },
            fail:function(res){
              console.log('登录失败' + res)
            }
          })
        } else {
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              console.log('user:' + res.userInfo.avatarUrl);
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  globalData: {
    userInfo: null,
    openid: null
  }
})