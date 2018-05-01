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
                data.user = $("#user").val();//自定义参数
                console.log($("#user").val());
                data.studentQQ = $("#studentQQ").val();
                data.qunName = $("#qunName").val();
                data.qunNum = $("#qunNum").val();
                data.stage = $("#stage option:selected").val();
                data.mark = $("#mark option:selected").val();
                return JSON.stringify(data);//默认参数
            },
            dataType: "json",
            processData: false,
            async: false,
            contentType: 'application/json;charset=UTF-8'

        },

        columns: [//对接收到的json格式数据进行处理，data为json中对应的key
            {data: "id"},
            {data: "studentQQ"},
            {data: "qunName"},
            {data: "qunNum"},
            {
                data: null, render: function (data, type, row, meta) {
                    switch (row.stage) {
                        case 0:
                            return "未跟进";
                            break;
                        case 1:
                            return "已跟进";
                            break;
                        default :
                            return "未跟进";
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
                        return '<a class="btn btn-info" id="' + row.id + '" onclick="changeStage(' + row.id + ')" disabled="true">已回访</a>';
                    } else {
                        return '<a class="btn btn-info" id="' + row.id + '" onclick="changeStage(' + row.id + ')">回访</a>';
                    }
                }
            },
            {
                data: null, render: function (data, type, row, meta) {
                    switch (row.mark) {
                        case 1:
                           return  '<a  class="btn btn-success" disabled="true">正常</a>';
                           break;
                        case 2:
                            return  '<a  class="btn btn-warning" disabled="true">未联系到-登陆正常</a>';
                            break;
                        case 3:
                            return  '<a  class="btn btn-danger" disabled="true">未联系到-长时间未登录</a>';
                            break;
                        case 4:
                            return  '<a  class="btn btn-info" disabled="true">账号过期</a>';
                            break;
                        case 5:
                            return  '<a  class="btn btn-default" disabled="true">其他</a>';
                            break;
                        default :
                            return '<div id="group' + row.id + '" class="btn-group-sm" role="group" aria-label="...">'
                                +'<a  class="btn btn-sm btn-success" id="' + row.id + '" onclick="changeMark(' + row.id +','+1+ ')">正常</a>'
                                +'<a  class="btn btn-sm btn-warning" id="' + row.id + '" onclick="changeMark(' + row.id + ','+2+')">未-登</a>'
                                +'<a  class="btn btn-sm btn-danger" id="' + row.id + '" onclick="changeMark(' + row.id +','+3+ ')">未-未</a>'
                                +'<a  class="btn btn-sm btn-info" id="' + row.id + '" onclick="changeMark(' + row.id + ','+4+')">账号过期</a>'
                                +'<a  class="btn btn-sm btn-default" id="' + row.id + '" onclick="changeMark(' + row.id + ','+5+')">其他</a>'
                           +' </div>';
                    }
                }
            },
            {
                data: null, render: function (data, type, row, meta) {
                    return '<a class="btn btn-danger" onclick="deleteStudent(' + row.id + ')">删除</a>';
                }
            },
            {data: "user"}
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
    layer.confirm("确定删除？", {btn: ["确定", "取消"]},
        function () {
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
        function () {
            layer.closeAll();
        });

}

<!-- 修改学员状态-->
function changeStage(id) {
    $.ajax({
        type: 'POST',
        url: "/student/changeStage",
        data: {'id': id},
        success: function (data) {
            if (data.data.stage == 1) {
                $("#" + id).attr('disabled', "true");
                $("#" + id).html("已回访");
                layer.msg('进行回访完毕');
            }
        }
    });
}
    <!-- 修改学员标签-->
    function changeMark(id, mark) {
        console.log(id);
        $.ajax({
            type: 'POST',
            url: "/student/changeMark",
            data: {'id':id, "mark": mark},
            success: function (data) {
                if (data.status == 200) {
                    switch (mark) {
                        case 1:
                            $('#group'+id).empty();
                            $('#group'+id).append('<a  class="btn btn-success" disabled="true">正常</a>');
                            break;
                        case 2:
                            $('#group'+id).empty();
                            $('#group'+id).append('<a  class="btn btn-warning" disabled="true">未联系到-登陆正常</a>');
                            break;
                        case 3:
                            $('#group'+id).empty();
                            $('#group'+id).append('<a  class="btn btn-danger" disabled="true">未联系到-长时间未登录</a>');
                            break;
                        case 4:
                            $('#group'+id).empty();
                            $('#group'+id).append('<a  class="btn btn-info" disabled="true">账号已过期</a>');
                            break;
                        case 5:
                            $('#group'+id).empty();
                            $('#group'+id).append('<a  class="btn btn-info" disabled="true">其他</a>');
                            break;
                    }
                    layer.msg('备注成功');
                }
            }
        });
}
