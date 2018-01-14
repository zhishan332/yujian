//index.js
const util = require('../../utils/util.js')

//获取应用实例
const app = getApp()
var touchDot = 0;//触摸时的原点
var time = 0;// 时间记录，用于滑动时且时间小于1s则执行左右滑动
var interval = "";// 记录/清理 时间记录
var imgLoadOk = false;
var loadOver = true;
Page({
  data: {
    images: {},

    currentPage: 1,

    url1: 'http://n.sinaimg.cn/ent/4_img/upload/d932d205/w722h1024/20180107/hpwx-fyqincu9914477.jpg',
    url2: 'https://img.onvshen.com:85/gallery/24515/22906/s/002.jpg',
    url3: 'http://c4.haibao.cn/img/600_0_100_1/1513843724.0094/00822c22c782d7c365235ac28557021e.jpeg',
    imageUrl: 'http://n.sinaimg.cn/ent/4_img/upload/d932d205/w722h1024/20180107/hpwx-fyqincu9914477.jpg',

    lastX: 0,

    lastY: 0,

    imagewidth: 0,//缩放后的宽

    imageheight: 0,//缩放后的高

    imagepadding: 0,
    userInfo: null,

    hasUserInfo: false,

    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },

  // 触摸开始事件
  touchStart: function (e) {
    touchDot = e.touches[0].pageX; // 获取触摸时的原点

    // 使用js计时器记录时间
    interval = setInterval(function () {

      time++;
    }, 100);

  },
  // 触摸移动事件
  touchMove: function (e) {
    var touchMove = e.touches[0].pageX;
  
    // 向左滑动
    if (touchMove - touchDot <= -40 && loadOver) {
      loadOver = false;
      wx.request({
        url: 'http://localhost:8080/taglist', //仅为示例，并非真实的接口地址
        data: {
          x: '',
          y: ''
        },
        header: {
          'content-type': 'application/json' // 默认值
        },
        success: function (res) {
          console.log(res.data)
        },
        complete:function(res){
          loadOver = true;
        }
      }),

      console.log("touchMove:" + touchMove + " touchDot:" + touchDot + " diff:" + (touchMove - touchDot));
      this.setData({
        url1: 'http://c3.haibao.cn/img/600_0_100_0/1514527662.7601/9de075d7bc0cbac6a6b513c36d8e7545.jpg',
        url2: 'http://c3.haibao.cn/img/600_0_100_0/1514535970.8545/97ba25f60f5e7dd0572266af0fffcd36.jpg',
        url3: 'http://n.sinaimg.cn/ent/4_img/upload/d932d205/w740h1024/20180107/1G7C-fyqincu9914500.jpg',
        currentPage: 1
      });
    }
    // 向右滑动
    if (touchMove - touchDot >= 40) {
      console.log("touchMove:" + touchMove + " touchDot:" + touchDot + " diff:" + (touchMove - touchDot));
      var cup = this.data.currentPage;
      if (cup < 3) {
        this.setData({
          currentPage: cup + 1
        });
      } else {
        this.setData({
          url1: 'http://c1.haibao.cn/img/600_0_100_0/1514367106.359/4fa0e7bdce3b6fa974d4a549edd9c4c0.jpg',
          url2: 'http://c4.haibao.cn/img/600_0_100_0/1514367108.0392/68f02b78acb3bfb1a3b7dab884aeb4dc.jpg',
          url3: 'http://c4.haibao.cn/img/600_0_100_0/1514367112.7627/63eba430d637cf53c5850594caefa68e.jpg',
          currentPage: 1,
        });
      }
    }
    // touchDot = touchMove; //每移动一次把上一次的点作为原点（好像没啥用）
  },
  // 触摸结束事件
  touchEnd: function (e) {
    clearInterval(interval); // 清除setInterval
    time = 0;

  },
  handletap: function (event) {

    console.log(event)
  },
  imageLoad: function (e) {

    imgLoadOk = true;

    console.log(111111111);
  },
  //事件处理函数
  bindViewTap: function () {

    wx.navigateTo({
      url: '../logs/logs'

    })
  },
  onLoad: function () {

    if (app.globalData.userInfo) {

      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true

      });
      if ('zhishan' == this.data.userInfo) {

        console.log('zhishan is load...')
      }
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
  }
})