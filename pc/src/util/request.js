import axios from 'axios'
import url from '@/api/url.js'
import Cookies from 'js-cookie'
import { linkAdfsLogin as linkAdfsLoginApi } from "@/api/index";
axios.defaults.headers['Content-Type'] = 'application/json'
// 创建axios实例

const service = axios.create({
  // axios中请求配置有baseURL选项，表示请求URL公共部分
  // baseURL:'/api',
  // 超时
  timeout: 10000,
  withCredentials: true,
})

// request拦截器
service.interceptors.request.use(
  config => {
    // do something before request is sent
    // Cookies
    var token  = config.headers["X-XSRF-TOKEN"]
    // JSESSIONID=SOe7jEf5hNjZCeQdumVH-ekcfxNd6kpvMAITeXgO; XSRF-TOKEN=6c002ab9-cbd9-4e88-a321-cf452eb6f9a0
    // console.log(token);

    //本地测试代码
    // Cookies.set("JSESSIONID",'THGBDehh3jB-YrgHyOH5yA5ISauDsg5rOpRlYbRt');
    // config['Host'] = 'smegasc.cuhk.edu.cn';
    // config['Origin'] = 'https://smegasc.cuhk.edu.cn';
    // config['Referer'] = 'https://smegasc.cuhk.edu.cn/';
    // config['User-Agent'] = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36';
    // config['Sec-Fetch-Site'] = 'same-origin';
    // config['Sec-Fetch-Dest'] = 'empty';
    // config['Sec-Fetch-Mode'] = 'cors';
    // config['sec-ch-ua-mobile'] = '?0';
    // config['sec-ch-ua'] = '"Chromium";v="92", " Not A;Brand";v="99", "Google Chrome";v="92"';

    return config;
  },
  error => {
    // do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use( response => {
    const res =  response.data;
    if (res.status !== 200) {
      if (
        res.status === 40001 ||
        res.status === 40101 ||
        res.status === 40301 ||
        res.status === 40331
      ) {
        Cookies.remove('XSRF-TOKEN');
        window.localStorage.clear();
        return;
      }
    }
    return res;
  },
  error => {
    console.debug(error)
    console.log('err' + error)
    // 跳转ADFS 登录
    if(error.response.status === 401){
      linkAdfsLoginApi();
    }
    return Promise.reject(error)
  }
)


export default service
