// user.js
const app = getApp()
Page({
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    payCode:"http://localhost:8080/statics/img/shoukuan.JPG",
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    warnSize: 'default',
    primarySize: 'default',
    disabled: false,
    plain: false,
    loading: false,
    menuitems: [
      { text: '账号信息', url: '../userinfo/userinfo', icon: '../../images/usermenu/info.png', tips: '' },
      { text: '预订单', url: '../borrowbook/borrowbook?status=N', icon: '../../images/usermenu/order.png', tips: '' },
      { text: '借阅历史', url: '../borrowbook/borrowbook?status=F', icon: '../../images/usermenu/history.png', tips: '' },
      { text: '待归还', url: '../borrowbook/borrowbook?status=Y', icon: '../../images/usermenu/huan.png', tips: '' },
      { text: '个人喜好', url: '../favorcate/favorcate', icon: '../../images/usermenu/favor.png', tips: '' },
    ]
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {
    console.log('user:' + app.globalData.userInfo);
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      });
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function (e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
   
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})