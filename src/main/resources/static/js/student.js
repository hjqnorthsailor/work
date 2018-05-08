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
                    return getMark(row.id,row.mark);
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
    var table=$('#student_table').DataTable();
    layer.confirm("确定删除？", {btn: ["确定", "取消"]},
        function () {
            $.ajax({
                type: 'POST',
                url: "/student/delete",
                data: {'id': id},
                success: function (data) {
                    if (data.status = 200) {
                        table.row($('#group'+id).parents('tr')).remove().draw();
                        layer.msg('id'+id+'删除成功,刷新后显示结果');
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
        $.ajax({
            type: 'POST',
            url: "/student/changeMark",
            data: {'id':id, "mark": mark},
            success: function (data) {
                if (data.status == 200) {
                    switch (mark) {
                        case 1:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,1));
                            break;
                        case 2:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,2));
                            break;
                        case 3:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,3));
                            break;
                        case 4:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,4));
                            break;
                        case 5:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,5));
                            break;
                    }
                    layer.msg('备注成功');
                }
            }
        });
}
<!-- 恢复标签状态-->
function reset(id) {
    $.ajax({
        type: 'POST',
        url: "/student/reset",
        data: {'id': id},
        success: function (data) {
            if (data.status=200) {
                $('#group'+id).empty();
                $('#group'+id).append(getMark(id,0));
                layer.msg('恢复状态完毕');
            }
        }
    });
    
}
<!-- 获取标签 -->
function getMark(id,mark) {

    var mark1 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-warning" disabled="true">正常</a>'
        + '<a class="button button-small button-plain button-border button-circle" onclick="reset(' + id +')"><i class="fa fa-reply"></i></a>'
        + '</div>';
    var mark2 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-warning" disabled="true">未联系到-登陆正常</a>'
        + '<a class="button button-small button-plain button-border button-circle" onclick="reset(' + id + ')"><i class="fa fa-reply"></i></a>'
        + '</div>';
    var mark3 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-danger" disabled="true">未联系到-长时间未登录</a>'
        + '<a class="button button-small button-plain button-border button-circle" onclick="reset(' + id + ')"><i class="fa fa-reply"></i></a>'
        + '</div>';
    var mark4 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-info" disabled="true">账号过期</a>'
        + '<a class="button button-small button-plain button-border button-circle" onclick="reset(' + id +')"><i class="fa fa-reply"></i></a>'
        + '</div>';
    var mark5 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-default" disabled="true">其他</a>'
        + '<a   class="button button-small button-plain button-border button-circle" onclick="reset(' + id +')"><i class="fa fa-reply"></i></a>'
        + '</div>';
    var defaultMark = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-sm btn-success" id="' + id + '" onclick="changeMark(' + id + ',' + 1 + ')">正常</a>'
        + '<a  class="btn btn-sm btn-warning" id="' + id + '" onclick="changeMark(' + id + ',' + 2 + ')">未-登</a>'
        + '<a  class="btn btn-sm btn-danger"  id="' + id + '" onclick="changeMark(' + id + ',' + 3 + ')">未-未</a>'
        + '<a  class="btn btn-sm btn-info"    id="' + id + '" onclick="changeMark(' + id + ',' + 4 + ')">账号过期</a>'
        + '<a  class="btn btn-sm btn-default" id="' + id + '" onclick="changeMark(' + id + ',' + 5 + ')">其他</a>'
        + '</div>';
    switch (mark){
        case 1:
            return mark1;
            break;
        case 2:
            return mark2;
            break;
        case 3:
            return mark3;
            break;
        case 4:
            return mark4;
            break;
        case 5:
            return mark5;
            break;
        default:
            return defaultMark;

    }
}