import{b as v,w as b,g as h,e as S,a2 as T,m as x,S as C}from"./index.5d7be89b.js";import{g as P,e as w,f as E}from"./QList.f41b5876.js";const{passive:m}=C,M=["both","horizontal","vertical"];var Q=v({name:"QScrollObserver",props:{axis:{type:String,validator:e=>M.includes(e),default:"vertical"},debounce:[String,Number],scrollTarget:{default:void 0}},emits:["scroll"],setup(e,{emit:p}){const t={position:{top:0,left:0},direction:"down",directionChanged:!1,delta:{top:0,left:0},inflectionPoint:{top:0,left:0}};let n=null,i,s;b(()=>e.scrollTarget,()=>{f(),u()});function c(){n!==null&&n();const r=Math.max(0,w(i)),a=E(i),o={top:r-t.position.top,left:a-t.position.left};if(e.axis==="vertical"&&o.top===0||e.axis==="horizontal"&&o.left===0)return;const g=Math.abs(o.top)>=Math.abs(o.left)?o.top<0?"up":"down":o.left<0?"left":"right";t.position={top:r,left:a},t.directionChanged=t.direction!==g,t.delta=o,t.directionChanged===!0&&(t.direction=g,t.inflectionPoint=t.position),p("scroll",{...t})}function u(){i=P(s,e.scrollTarget),i.addEventListener("scroll",l,m),l(!0)}function f(){i!==void 0&&(i.removeEventListener("scroll",l,m),i=void 0)}function l(r){if(r===!0||e.debounce===0||e.debounce==="0")c();else if(n===null){const[a,o]=e.debounce?[setTimeout(c,e.debounce),clearTimeout]:[requestAnimationFrame(c),cancelAnimationFrame];n=()=>{o(a),n=null}}}const{proxy:d}=x();return h(()=>{s=d.$el.parentNode,u()}),S(()=>{n!==null&&n(),f()}),Object.assign(d,{trigger:l,getPosition:()=>t}),T}});export{Q};