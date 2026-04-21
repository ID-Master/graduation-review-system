import Vue from 'vue'
import Router from 'vue-router'
import login from '@/components/login'
import Richtext from "@/components/Richtext"
import student from "@/components/student"
import config from '@/components/config';
import report from '@/components/report';
import preview from '@/components/preview';
import result from '@/components/result';
Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'login',
      component: login
    },
    {
      path: '/list',
      name: 'list',
      component: Richtext
    },
    {
      path: '/student',
      name: 'student',
      component: student
    },
    {
      path: '/config',
      name: 'config',
      component: config
    },
    {
      path: '/report',
      name: 'report',
      component: report
    },
    {
      path: '/preview',
      name: 'preview',
      component: preview
    },
    {
      path: '/result',
      name: 'result',
      component: result
    }
  ]
})
