//index.js
const config = require('../../conf.js');
const util = require('../../utils/util.js');
const hSwiper = require('hSwiper.js');
const request = require('../../utils/request.js');

//获取应用实例
const app = getApp()
var swiper;
var option = {
  data: {
    showLoading: false,
    loadingMessage: '',
    showToast: false,
    toastMessage: '',
    laodMore: true,
    //swiper插件变量
    hSwiperVar: {}
  },
  showLoading(message) {
    this.setData({ showLoading: true, loadingMessage: message });
  },
  hideLoading() {
    this.setData({ showLoading: false, loadingMessage: '' });
  },
  showToast(toastMessage) {
    this.setData({ showToast: true, toastMessage });
  },
  hideToast() {
    this.setData({ showToast: false, toastMessage: '' });
  },
  loadMore() {
    var oldList = swiper.getList();
    this.fetchImgs().then((resp) => {
      this.hideLoading();
      var imgList = [];
      if (resp.status != 1) {
        this.showToast('加载失败，请刷新重试...');
        return;
      }
      if (util.isEmpty(resp.data)) {
        this.showToast('没有最新的了...');
        return;
      }
      for (var index in resp.data) {
        oldList.push(util.imgUrlFix(resp.data[index].url));
      }
      swiper.updateList(oldList);
      this.setData({ laodMore: true });

      console.log('更新OVER');
    });
  },
  initSwiper(imgList) {
    swiper = new hSwiper({ reduceDistance: 20, varStr: 'hSwiperVar', list: imgList });
    swiper.onFirstView = function (data, index) {
    };
    var _this = this;
    swiper.onLastView = function (data, index) {
      //console.log('当前是第' + (index + 1) + '视图', '数据是：' + data);

      if (_this.data.laodMore) {
        console.log('开始更新');
        _this.setData({ laodMore: false });
        _this.loadMore();
      }

      // wx.request({
      //   url: util.getUrl('/find', [{ openId: '' }]), //仅为示例，并非真实的接口地址
      //   data: {
      //   },
      //   header: {
      //     'content-type': 'application/json' // 默认值
      //   },
      //   success: function (respAdd) {
      //     var imgListAdd = [];
      //     if (respAdd.status != 1) {
      //       return;
      //     }
      //     if (util.isEmpty(respAdd.data)) {
      //       return;
      //     }
      //     for (var index in respAdd.data) {
      //       oldList.concat(util.imgUrlFix(respAdd.data[index].url));
      //     }

      //   }
      // })
    };
    swiper.afterViewChange = function (data, index) {
    };
    swiper.beforeViewChange = function (data, index) {
    };
  },
  onLoad: function () {

  },
  loadImg() {
    console.log(111111111111);
    this.fetchImgs().then((resp) => {
      this.hideLoading();
      var imgList = [];
      if (resp.status != 1) {
        this.showToast('加载失败，请刷新重试...');
        return;
      }
      if (util.isEmpty(resp.data)) {
        this.showToast('没有最新的了...');
        return;
      }
      for (var index in resp.data) {
        imgList.push(util.imgUrlFix(resp.data[index].url));
      }
      this.initSwiper(imgList);
    });
  },
  onReady: function () {
    this.loadImg();
    //更新数据
    // setTimeout(() => {
    //   console.log('5s后更新数据 并且更新视图');
    //   //5s后更新数据 并且更新视图
    //   var oldList = swiper.getList();
    //   swiper.updateList(oldList.concat([11, 23, 45]));
    // }, 5000);
  },
  bindViewTap: function (event) {
    swiper.nextView();
  },
  fetchImgs() {
    this.showLoading('加载中...');
    return request({ method: 'GET', url: util.getUrl('/find', [{ openId: '' }]) });
  }
};

Page(option);