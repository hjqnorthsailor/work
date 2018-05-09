$(function () {
    var startTime;var endTime;
    $(function() {//http://www.daterangepicker.com/#options
        $('input[name="datefilter"]').daterangepicker({
            autoUpdateInput: false,
            locale: {
                applyLabel: "应用",
                cancelLabel: "取消",
                resetLabel: "重置",
            }
        }, function(start, end, label) {
            console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
            startTime=start.format('YYYY-MM-DD');
            endTime=end.format('YYYY-MM-DD');
        });
    });
    $('input[name="datefilter"]').on('apply.daterangepicker', function(ev, picker) {
        $(this).val(picker.startDate.format('YYYY-MM-DD') + ' - ' + picker.endDate.format('YYYY-MM-DD'));
    });

    $('input[name="datefilter"]').on('cancel.daterangepicker', function(ev, picker) {
        $(this).val('');
    });
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
                data.startTime=startTime;
                data.endTime=endTime;
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
                    return getMark(row.id,row.mark,row.remark);
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
    function changeMark(id, mark,remark) {
        $.ajax({
            type: 'POST',
            url: "/student/changeMark",
            data: {'id':id, "mark": mark},
            success: function (data) {
                if (data.status == 200) {
                    switch (mark) {
                        case 1:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,1,remark));
                            break;
                        case 2:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,2,remark));
                            break;
                        case 3:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,3,remark));
                            break;
                        case 4:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,4,remark));
                            break;
                        case 5:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,5,remark));
                            break;
                        case 6:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,6,remark));
                            break;
                        case 7:
                            $('#group'+id).empty();
                            $('#group'+id).append(getMark(id,7,''));
                            break;

                    }
                    layer.msg('备注成功');
                }
            }
        });
}
<!-- 恢复标签状态-->
function reset(id,remark) {
    $.ajax({
        type: 'POST',
        url: "/student/reset",
        data: {'id': id},
        success: function (data) {
            if (data.status=200) {
                $('#group'+id).empty();
                console.log(id);
                $('#group'+id).append(getMark(id,0,remark));
                layer.msg('恢复状态完毕');
            }
        }
    });

}
function remark(id,remark) {
    $('#myModal').modal('show');
    $('#modalBody').empty();
    $('#modalBody').append('<input type="hidden" name="id" value="'+id+'"/>'
        +'<textarea id="area'+id+'" class="form-control" rows="3" name="remark"></textarea>');
    $('#area'+id).html(remark);
    $('#remarkBtn').attr('onclick','submitRemark('+id+');');

}
function submitRemark(id) {
    var remark=$('#area'+id).html();
    $.ajax({
        type: 'POST',
        url: "/student/remark",
        data: {'id': id,'remark':remark},
        success: function (data) {
            if (data.status=200) {
                layer.msg('备注成功');
                $('#myModal').modal('hide');
            }
        }
    });
}
<!-- 获取标签 -->
function getMark(id,mark,remark) {
    var mark1 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-success" disabled="true">正常</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';

    var mark2 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-warning" disabled="true">未联系到-登陆正常</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var mark3 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-danger" disabled="true">未联系到-长时间未登录</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var mark4 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-info" disabled="true">账号过期</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var mark5 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-default" disabled="true">转脱产</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var mark6 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-default" disabled="true">查询不到</a>'
        + '<a  class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var mark7 = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-primary" disabled="true">其他</a>'
        + '<a   class="button-plain button-border button-circle" onclick="reset(' + id +',\'' +remark+'\')"><i class="fa fa-reply"></i></a>'
        + '<a  class="button-plain button-border button-circle" onclick="remark(' + id +',\'' +remark+'\')"><i class="fa fa-plus"></i></a>'
        + '</div>';
    var defaultMark = '<div id="group' + id + '" class="btn-group-sm" role="group" aria-label="...">'
        + '<a  class="btn btn-xs btn-success" id="' + id + '" onclick="changeMark(' + id + ',' + 1 +',\'' +remark+'\')" title="在读(未转方向)">正常</a>'
        + '<a  class="btn btn-xs btn-warning" id="' + id + '" onclick="changeMark(' + id + ',' + 1 +',\'' +remark+'\')" title="未联系到，登录正常">未-登</a>'
        + '<a  class="btn btn-xs btn-danger"  id="' + id + '" onclick="changeMark(' + id + ',' + 3 +','+remark+')" title="未联系到，超过两月未登录">未-未</a>'
        + '<a  class="btn btn-xs btn-info"    id="' + id + '" onclick="changeMark(' + id + ',' + 4 +','+remark+')" title="账号已过期，学员回访查不到">过期</a>'
        + '<a  class="btn btn-xs btn-default" id="' + id + '" onclick="changeMark(' + id + ',' + 5 +','+remark+')" title="已转脱产">脱产</a>'
        + '<a  class="btn btn-xs btn-default" id="' + id + '" onclick="changeMark(' + id + ',' + 6 +','+remark+')" title="学员回访和学员管理都无法查询到">查询不到</a>'
        + '<a  class="btn btn-xs btn-primary" id="' + id + '" onclick="changeMark(' + id + ',' + 7 +','+remark+')" title="试听、休学">其他</a>'
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
        case 6:
            return mark6;
            break;
        case 7:
            return mark7;
            break;
        default:
            return defaultMark;

    }
}