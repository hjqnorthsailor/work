$(function () {
    var table = $('#student_table').DataTable({
        dom: 'rtlip',//布局
        lengthMenu: [10, 20, 30, 50, 100],//每页多少条数据
        destroy: true,
        paging: true,//分页
        serverSide: true,//服务器模式
        autoWidth: true,//自适应宽度
        deferRender: true,
        ajax: {
            url: "/student/findAll",
            type: "POST",
            data: function (data) {//参数
                data.studentName = $("#studentName").val();//自定义参数
                data.studentQQ = $("#studentQQ").val();
                data.qunName = $("#qunName").val();
                data.qunNum = $("#qunNum").val();
                data.stage = $("#stage option:selected").val();
                return JSON.stringify(data);//默认参数
            },
            dataType: "json",
            processData: false,
            async: false,
            contentType: 'application/json;charset=UTF-8'

        },

        columns: [//对接收到的json格式数据进行处理，data为json中对应的key
            {data: "id"},
            {data: "studentName"},
            {data: "studentQQ"},
            {data: "qunName"},
            {data: "qunNum"},
            {
                data: null, render: function (data, type, row, meta) {
                    switch (row.stage) {
                        case 0:
                            return "未跟进"
                            break;
                        case 1:
                            return "已跟进"
                            break;
                        default :
                            return "未跟进"
                    }
                }
            },
            {
                data: null, render: function (data, type, row, meta) {
                    return '<a class="btn btn-info"  target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=' + row.studentQQ + '&amp;site=qq&amp;menu=yes">聊天</a>';
                }
            },
            {
                data: null, render: function (data, type, row, meta) {
                    if (row.stage == 1) {
                        return '<a class="btn btn-info" id="' + row.studentQQ + '" onclick="changeStage(' + row.studentQQ + ')" disabled="true">已回访</a>';
                    } else {
                        return '<a class="btn btn-info" id="' + row.studentQQ + '" onclick="changeStage(' + row.studentQQ + ')">回访</a>';
                    }
                }
            },
            {
                data: null, render: function (data, type, row, meta) {
                    return '<a class="btn btn-danger" onclick="deleteStudent(' + row.id + ')">删除</a>';
                }
            }
        ],
        language: { //中文支持
            sUrl: "/webjars/datatables-plugins/1.10.16/i18n/Chinese.json"
        }
    });
    $("#searchBtn").bind("click", function () {
        table.draw();
    });
});
<!--删除学员 -->
function deleteStudent(id) {
    layer.confirm("确定删除？", {btn:["确定","取消"]},
        function(){
            $.ajax({
                type: 'POST',
                url: "/student/delete",
                data: {'id': id},
                success: function (data) {
                    if (data.status = 200) {
                        layer.msg('删除成功');
                    }
                }
            });
        },
        function(){
            layer.closeAll();
        });

}
<!-- 修改学员状态-->
function changeStage(qq) {
    $.ajax({
        type: 'POST',
        url: "/student/changeStage",
        data: {'studentQQ': qq},
        success: function (data) {
            if (data.data.stage == 1) {
                $("#" + qq).attr('disabled', "true");
                $("#" + qq).html("已回访");
                layer.msg('已对'+qq+'进行回访');
            }
        }
    });
}
