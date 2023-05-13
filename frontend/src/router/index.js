import { route } from 'quasar/wrappers'
import { createRouter, createMemoryHistory, createWebHistory, createWebHashHistory } from 'vue-router'
import { useAuthStore } from 'stores/auth.store.js' 
import routes from './routes'

/*
 * If not building with SSR mode, you can
 * directly export the Router instantiation;
 *
 * The function below can be async too; either use
 * async/await or return a Promise which resolves
 * with the Router instance.
 */

export default route(function (/* { store, ssrContext } */) {
  const createHistory = process.env.SERVER
    ? createMemoryHistory
    : (process.env.VUE_ROUTER_MODE === 'history' ? createWebHistory : createWebHashHistory)

  const Router = createRouter({
    scrollBehavior: () => ({ left: 0, top: 0 }),
    routes,

    // Leave this as is and make changes in quasar.conf.js instead!
    // quasar.conf.js -> build -> vueRouterMode
    // quasar.conf.js -> build -> publicPath
    history: createHistory(process.env.VUE_ROUTER_BASE)
  })


  Router.beforeEach(async (to, from) => {
    const authStore = useAuthStore()
    authStore.init()
  

      if(!authStore.user.accessToken && to.name !== 'auth') {
        return { name: 'auth'}
      }

      // if(authStore.user.accessToken && from.name === 'auth') {
      //   return {name: 'projects'}
      // }
  
      if(authStore.user.accessToken && to.name === 'auth') {
        if(from.name === undefined)
          return {name: 'projects'}
        else  
          return false
      }

 

  })

  return Router
})