var nodes = new Vue({
	el : '#divs',
	data : {
		scaling:150,//缩放比例
	    nodes : [], //所有节点
		lines:[],//所有的线
    },
	mounted : function mounted() {

	    this.initDate();
    },
	methods : {
		initDate : function (){
		    var data =  demo.testMethod();
		    data = eval("("+data+")");
		    this.nodes = data;
            this.GetLine(this.nodes);

		},

        callJS : function (str){
            var data = demo.testMethod();
            var  d = eval("("+data+")");

       },
		//画线
		GetLine : function (data) {
			if (data.length > 0) {
				var obj = {};
				//将list封装成map格式，方便根据id查找父节点
				for (var i = 0 ; i < data.length; i++ ) {
					var rec = data[i];
					var id = rec.id;
					obj[id] = rec;
				}
				var result = [];
				//遍历节点数组，对数组重新封装成[{sunNode:{},parentNode:{}}]格式
				for(var i = 0 ; i < data.length; i++){
					var rec = data[i];
					var parentKeys = rec.nodesupe;
					var parentKey = parentKeys.split(",");
					if(parentKeys){
						for(var j = 0 ; j < parentKey.length; j++){
							var parent = obj[parentKey[j]];
							if(parent){
								var res = {
										sunNode : rec,
										parentNode : parent
								}
								result.push(res);
							}
						}
					}
				}
				this.lines = result;
			}
		}

	}
})