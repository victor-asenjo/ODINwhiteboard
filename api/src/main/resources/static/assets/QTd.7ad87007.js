import{b as c,a as u,h as s,f as r,m as v}from"./index.5d7be89b.js";var p=c({name:"QTd",props:{props:Object,autoWidth:Boolean,noHover:Boolean},setup(t,{slots:e}){const l=v(),a=u(()=>"q-td"+(t.autoWidth===!0?" q-table--col-auto-width":"")+(t.noHover===!0?" q-td--no-hover":"")+" ");return()=>{if(t.props===void 0)return s("td",{class:a.value},r(e.default));const d=l.vnode.key,o=(t.props.colsMap!==void 0?t.props.colsMap[d]:null)||t.props.col;if(o===void 0)return;const{row:n}=t.props;return s("td",{class:a.value+o.__tdClass(n),style:o.__tdStyle(n)},r(e.default))}}});export{p as Q};
