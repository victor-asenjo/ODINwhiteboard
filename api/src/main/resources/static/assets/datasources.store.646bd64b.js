import{b as _,a as x,h as q,f as N,k as U,m as K,l as M,ab as V,aq as l,ar as Q,as as E,u as i,at as G,au as g}from"./index.5d7be89b.js";var W=typeof globalThis!="undefined"?globalThis:typeof window!="undefined"?window:typeof global!="undefined"?global:typeof self!="undefined"?self:{},Y=_({name:"QItemLabel",props:{overline:Boolean,caption:Boolean,header:Boolean,lines:[Number,String]},setup(e,{slots:o}){const t=x(()=>parseInt(e.lines,10)),n=x(()=>"q-item__label"+(e.overline===!0?" q-item__label--overline text-overline":"")+(e.caption===!0?" q-item__label--caption text-caption":"")+(e.header===!0?" q-item__label--header":"")+(t.value===1?" ellipsis":"")),r=x(()=>e.lines!==void 0&&t.value>1?{overflow:"hidden",display:"-webkit-box","-webkit-box-orient":"vertical","-webkit-line-clamp":t.value}:null);return()=>q("div",{style:r.value,class:n.value},N(o.default))}}),Z=_({name:"QPage",props:{padding:Boolean,styleFn:Function},setup(e,{slots:o}){const{proxy:{$q:t}}=K(),n=U(M);U(V,()=>{console.error("QPage needs to be child of QPageContainer")});const r=x(()=>{const c=(n.header.space===!0?n.header.size:0)+(n.footer.space===!0?n.footer.size:0);if(typeof e.styleFn=="function"){const m=n.isContainer.value===!0?n.containerHeight.value:t.screen.height;return e.styleFn(c,m)}return{minHeight:n.isContainer.value===!0?n.containerHeight.value-c+"px":t.screen.height===0?c!==0?`calc(100vh - ${c}px)`:"100vh":t.screen.height-c+"px"}}),u=x(()=>`q-page${e.padding===!0?" q-layout-padding":""}`);return()=>q("main",{class:u.value,style:r.value},N(o.default))}}),h={getAll(e,o){return l.get("/project/"+e+"/datasources",{headers:{Authorization:`Bearer ${o}`}})},createDSPersistent(e,o,t){return l.post("/project/"+e+"/datasources/persist",o,{headers:{Authorization:`Bearer ${t}`}})},deleteDS(e,o,t){return l.delete("/project/"+e+"/datasources/"+o,{headers:{Authorization:`Bearer ${t}`}})},getTriples(e,o,t){return l.get("/project/"+e+"/datasources/triples/"+o,{headers:{Authorization:`Bearer ${t}`}})},getAllTemporal(e,o){return l.get("/project/"+e+"/temp/ds",{headers:{Authorization:`Bearer ${o}`}})},deleteTemporal(e,o,t){return l.delete("/project/"+e+"/temp/ds/"+o,{headers:{Authorization:`Bearer ${t}`}})},createDSTemp(e,o,t){return l.post("/project/"+e+"/temp/ds",t,{headers:{"Content-Type":"multipart/form-data",Authorization:`Bearer ${o}`}})},downloadSourceGraph(e,o,t){return l.get("/project/"+e+"/datasources/download/sourcegraph",{headers:{Authorization:`Bearer ${t}`},params:{dsID:o},responseType:"blob"})},downloadProjectGraph(e,o){return l.get("/project/"+e+"/integration/download/projectschema",{headers:{Authorization:`Bearer ${o}`},responseType:"blob"})}},B={integrateJoins(e,o,t){return l.post("/project/"+e+"/integration/join",o,{headers:{Authorization:`Bearer ${t}`}})},integrate(e,o,t){return l.post("/project/"+e+"/integration",o,{headers:{Authorization:`Bearer ${t}`}})},finishIntegration(e,o){return l.post("/project/"+e+"/integration/persist",null,{headers:{Authorization:`Bearer ${o}`}})},surveyAlignments(e,o,t){return l.post("/project/"+e+"/integration/survey",o,{headers:{"Content-Type":"text/plain",Authorization:`Bearer ${t}`}})},downloadSourceGraph(e,o,t){return l.get("/project/"+e+"/integration/download/sourcegraph",{headers:{Authorization:`Bearer ${t}`},params:{dsID:o},responseType:"blob"})}},H={exports:{}};(function(e,o){(function(t,n){e.exports=n()})(W,function(){return function t(n,r,u){var c=window,m="application/octet-stream",S=u||m,a=n,v=!r&&!u&&a,d=document.createElement("a"),k=function(s){return String(s)},p=c.Blob||c.MozBlob||c.WebKitBlob||k,y=r||"download",f,I;if(p=p.call?p.bind(c):Blob,String(this)==="true"&&(a=[a,S],S=a[0],a=a[1]),v&&v.length<2048&&(y=v.split("/").pop().split("?")[0],d.href=v,d.href.indexOf(v)!==-1)){var w=new XMLHttpRequest;return w.open("GET",v,!0),w.responseType="blob",w.onload=function(s){t(s.target.response,y,m)},setTimeout(function(){w.send()},0),w}if(/^data:([\w+-]+\/[\w+.-]+)?[,;]/.test(a))if(a.length>1024*1024*1.999&&p!==k)a=z(a),S=a.type||m;else return navigator.msSaveBlob?navigator.msSaveBlob(z(a),y):T(a);else if(/([\x80-\xff])/.test(a)){var b=0,P=new Uint8Array(a.length),J=P.length;for(b;b<J;++b)P[b]=a.charCodeAt(b);a=new p([P],{type:S})}f=a instanceof p?a:new p([a],{type:S});function z(s){var j=s.split(/[:;,]/),D=j[1],F=j[2]=="base64"?atob:decodeURIComponent,L=F(j.pop()),R=L.length,A=0,O=new Uint8Array(R);for(A;A<R;++A)O[A]=L.charCodeAt(A);return new p([O],{type:D})}function T(s,j){if("download"in d)return d.href=s,d.setAttribute("download",y),d.className="download-js-link",d.innerHTML="downloading...",d.style.display="none",document.body.appendChild(d),setTimeout(function(){d.click(),document.body.removeChild(d),j===!0&&setTimeout(function(){c.URL.revokeObjectURL(d.href)},250)},66),!0;if(/(Version)\/(\d+)\.(\d+)(?:\.(\d+))?.*Safari\//.test(navigator.userAgent))return/^data:/.test(s)&&(s="data:"+s.replace(/^data:([\w\/\-\+]+)/,m)),window.open(s)||confirm(`Displaying New Document

Use Save As... to download, then click back to return to this page.`)&&(location.href=s),!0;var D=document.createElement("iframe");document.body.appendChild(D),!j&&/^data:/.test(s)&&(s="data:"+s.replace(/^data:([\w\/\-\+]+)/,m)),D.src=s,setTimeout(function(){document.body.removeChild(D)},333)}if(navigator.msSaveBlob)return navigator.msSaveBlob(f,y);if(c.URL)T(c.URL.createObjectURL(f),!0);else{if(typeof f=="string"||f.constructor===k)try{return T("data:"+S+";base64,"+c.btoa(f))}catch{return T("data:"+S+","+encodeURIComponent(f))}I=new FileReader,I.onload=function(s){T(this.result)},I.readAsDataURL(f)}return!0}})})(H);var $=H.exports;const C=Q("integration",{state:()=>({project:{},projectTemporal:{},datasources:[],selectedDS:[],alignments:[],joinAlignments:[]}),getters:{getDatasourcesNumber(e){return e.datasources.length},getSourceB(e){return e.selectedDS.length==1?e.selectedDS[0]:null},getGraphicalA(e){return e.project.graphicalGlobalSchema},getGraphicalB(e){return e.selectedDS.length==1?e.selectedDS[0].graphicalSchema:""},getGlobalSchema(e){return e.projectTemporal.graphicalGlobalSchema?e.projectTemporal.graphicalGlobalSchema:""},getGraphicalSchemaIntegration(e){return e.projectTemporal.graphicalSchemaIntegration?e.projectTemporal.graphicalSchemaIntegration:""},isDSEmpty(e){return e.datasources.length==0},isJoinAlignmentsRelationshipsComplete(e){return e.joinAlignments.length==0||e.joinAlignments.filter(o=>o.relationship=="").length==0}},actions:{async init(){},async setProject(e){const o=E(),t=i();if(console.log("setting project to integration store.."),e)this.project=e;else if(!this.project.name){const n=await G.getProjectByID(o.params.id,t.user.accessToken);n.status==200&&(this.project=n.data)}t.user.accessToken&&(console.log("retrieving temporal data sources..."),this.getTemporalDatasources(),this.alignments=[])},deleteTemporalDS(e){const o=g(),t=i();h.deleteTemporal(this.project.id,e.id,t.user.accessToken).then(n=>{if(console.log("delete ds temporal"),console.log(n.data),n.status==204){let r=this.datasources.indexOf(e);r>-1?(console.log("dele index"),this.datasources.splice(r,1)):console.log("something wrong, could not find data source in array to delete it"),this.selectedDS.length>0&&this.selectedDS[0].id===e.id&&(console.log("data source deleter from selection"),this.selectedDS=[])}else console.log("check status!!! something wrong: ",n)}).catch(n=>{console.log("error deleting data sources"),console.log(n),o.negative("Error deleting data source")})},async getTemporalDatasources(){const e=g(),o=i();console.log("Pinia getting temporal data sources..."),await h.getAllTemporal(this.project.id,o.user.accessToken).then(t=>{console.log("ds temporal received",t),t.data===""?this.datasources=[]:this.datasources=t.data}).catch(t=>{console.log("error retrieving data sources"),console.log(t),e.negative("Cannot conect to the server.")})},addDataSource(e,o,t){const n=g(),r=i();console.log("adding data source...",o),h.createDSTemp(e,r.user.accessToken,o).then(u=>{console.log("createDSTemp()",u),u.status==201?(this.datasources.push(u.data),t(u.data)):n.negative("Cannot create datasource. Something went wrong in the server.")}).catch(u=>{console.log("error addding ds: ",u),n.negative("Something went wrong in the server.")})},addSelectedDatasource(e){this.selectedDS=[],this.selectedDS.push(e)},SelectOneDatasource(){this.datasources.length>0&&(this.selectedDS=[],this.selectedDS.push(this.datasources[0]))},deleteSelectedDatasource(e){console.log("deleteselect: ",e),console.log("sources ; ",this.selectedDS);let o=this.selectedDS.indexOf(e);o>-1?(console.log("dele index"),this.selectedDS.splice(o,1)):this.selectedDS=this.selectedDS.filter(t=>t.id!=e.id)},finishIntegration(e){this.deleteSelectedDatasource(e);let o=this.datasources.indexOf(e);o>-1?(console.log("dele index"),this.datasources.splice(o,1)):console.log("something wrong, could not find data source in array to delete it")},addAligment(e,o){console.log("alignment store: ",e);let t={};o?(t.iriA=e.resourceA.iri,t.labelA=e.resourceA.label,t.iriB=e.resourceB.iri,t.labelB=e.resourceB.label,t.l=e.integratedLabel,t.type=e.type):t=e,this.alignments.push(t)},integrateTemporal(e){const o=i(),t=g();var n={dsB:this.selectedDS[0],alignments:this.alignments};B.integrate(this.project.id,n,o.user.accessToken).then(r=>{console.log("integration response...",r),r.status==201||r.status?(t.positive("Integration succeeded"),this.projectTemporal=r.data.project,this.joinAlignments=r.data.joins,e&&e()):t.negative("There was an error for the integration task")}).catch(r=>{console.log("error integrating ds"),t.negative("Something went wrong in the server. No possible to integrate it")})},integrateJoins(e){const o=i(),t=g();this.joinAlignments.length>0&&B.integrateJoins(this.project.id,this.joinAlignments,o.user.accessToken).then(n=>{console.log("join integration response...",n),n.status==201||n.status?(t.positive("Integration succeeded"),this.projectTemporal=n.data,e&&e()):t.negative("There was an error for the integration task")}).catch(n=>{console.log("error integrating ds"),t.negative("Something went wrong in the server. No possible to integrate it")})},saveIntegration(e){const o=i(),t=g();console.log("save intregration store...",o.user.accessToken),console.log("project id ",this.project.id),B.finishIntegration(this.project.id,o.user.accessToken).then(n=>{console.log("integration response...",n),n.status==200?(e&&e(this.selectedDS[0]),t.positive("Integration saved successfully")):t.negative("There was an error to save the integration")}).catch(n=>{console.log("error saving integration",n),t.negative("Something went wrong in the server. No possible to save integration")})},deleteAligment(e){console.log("aligment is ",e);let o=this.alignments.indexOf(e);console.log("index is ",o),this.alignments.splice(o,1)},deleteJoinAlignment(e){console.log("join alignment is ",e);let o=this.joinAlignments.indexOf(e);console.log("index is ",o),this.joinAlignments.splice(o,1)},getAlignmentsSurvey(){console.log("getting alignments survey....",this.selectedDS[0].id);const e=i(),o=g();B.surveyAlignments(this.project.id,this.selectedDS[0].id,e.user.accessToken).then(t=>{console.log("survey alignments response...",t),t.status==200&&(this.alignments=t.data)}).catch(t=>{console.log("error alignments survye ",t),o.negative("Something went wrong in the server.")})},async downloadSourceTemporal(e){console.log("download....",e);const o=i(),t=await B.downloadSourceGraph(this.project.id,e,o.user.accessToken),n=t.headers["content-type"];$(t.data,"source_graph.ttl",n)}}}),ee=Q("datasource",{state:()=>({project:{},datasources:[]}),getters:{getDatasourcesNumber(e){return e.datasources.length},getGlobalSchema(e){return e.project.graphicalGlobalSchema?e.project.graphicalGlobalSchema:""},getGraphicalSchemaIntegration(e){return e.project.graphicalSchemaIntegration?e.project.graphicalSchemaIntegration:""}},actions:{async init(){},async setProject(e){const o=E(),t=i(),n=C();if(console.log("setting project to datasources store",e),e)console.log("if proj"),this.project=e,n.setProject(e);else if(!this.project.name||this.project.id!=o.params.id){console.log("dfs",o.params.id);const r=await G.getProjectByID(o.params.id,t.user.accessToken);r.status==200&&(this.project=r.data,n.setProject(this.project))}return t.user.accessToken&&(console.log("retrieving persistent data sources..."),this.getDatasources()),this.project},async getTriples(e,o){const t=i();return(await h.getTriples(e.id,o,t.user.accessToken)).data},async updateProjectInfo(){console.log("updating project info");const e=i(),o=C(),t=await G.getProjectByID(this.project.id,e.user.accessToken);t.status==200&&(this.project=t.data,o.setProject(this.project))},async getDatasources(){const e=g(),o=i();console.log("Pinia getting data sources..."),await h.getAll(this.project.id,o.user.accessToken).then(t=>{console.log("ds received",t.data),t.data===""?this.datasources=[]:this.datasources=t.data,console.log(this.datasources)}).catch(t=>{console.log("error retrieving data sources"),console.log(t),e.negative("Cannot conect to the server.")})},persistDataSource(e){const o=g(),t=i(),n=C();console.log("persist data source...",e),h.createDSPersistent(this.project.id,e,t.user.accessToken).then(r=>{console.log("createPersistentDS()",r),r.status==201?(this.datasources.push(r.data),n.finishIntegration(e),this.updateProjectInfo(),this.router.go(-1)):o.negative("Cannot integrate datasource with project. Something went wrong in the server.")}).catch(r=>{console.log("error integrating ds with project: ",r),o.negative("Something went wrong in the server.")})},deleteDataSource(e){const o=i(),t=g();h.deleteDS(this.project.id,e.id,o.user.accessToken).then(n=>{if(n.status==204){t.positive("Successfully deleted");let r=this.datasources.indexOf(e);r>-1&&(console.log("dele index"),this.datasources.splice(r,1)),this.updateProjectInfo()}else t.negative("Something went wrong in the server.")}).catch(n=>{console.log("error deleting data sources"),console.log(n),t.negative("Cannot delete data source. Error in the server.")})},async downloadSource(e){console.log("download....",e);const o=i(),t=await h.downloadSourceGraph(this.project.id,e,o.user.accessToken),n=t.headers["content-type"];$(t.data,"prueba.ttl",n)},async downloadProjectS(){console.log("download project....");const e=i(),o=await h.downloadProjectGraph(this.project.id,e.user.accessToken),t=o.headers["content-type"];$(o.data,"source_graph.ttl",t)}}});export{Z as Q,C as a,Y as b,ee as u};
