/**
 * Created with IntelliJ IDEA.
 * User: ZhengHongEn
 * Date: 13-7-15
 * Time: 下午2:15
 * 月日选择器
 */
$(function () {
    var monthDNum = new Array(0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);
    var monthArr = new Array("", "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月");
    var monthDayPickerContainer;

    initMonthDayPicker();

    function initMonthDayPicker() {
        $('.MonthDayPicker').each(function () {
            var _this = $(this);
            var initMonthDayPicker = _this.attr("initMonthDayPicker");
            if (!initMonthDayPicker) {
                //初始化控件区域
                var monthDayPickerContainer = $("<div></div>").appendTo(_this.parent());
                monthDayPickerContainer.addClass("monthDayPickerContainer");

                //初始化月份区域
                var monthContainer = $("<select></select>").appendTo(monthDayPickerContainer);
                monthContainer.addClass("monthContainer");

                //初始化日期区域
                var dayContainer = $("<select></select>").appendTo(monthDayPickerContainer);
                dayContainer.addClass("dayContainer");

                //循环显示月份
                for (var i = 1; i < monthArr.length; i++) {
                    $("<option value='" + i + "'>" + monthArr[i] + "</option>").appendTo(monthContainer);
                }

                //月份change事件
                monthContainer.change(function () {
                    var currentMonth = $(this).val();
                    dayContainer.empty();
                    for (var i = 1; i <= monthDNum[currentMonth]; i++) {
                        $("<option value='" + i + "'>" + i + "日</option>").appendTo(dayContainer);
                    }

                    setMonthDayVal(_this, currentMonth, dayContainer.val());
                }).change();

                //日期change事件
                dayContainer.change(function () {
                    var currentMonth = monthContainer.val();
                    var currentDay = $(this).val();
                    setMonthDayVal(_this, currentMonth, currentDay);
                }).change();

                //隐藏源input
                _this.hide();
                _this.attr("initMonthDayPicker", true);
            }
        });
    }

    function setMonthDayVal(obj, month, day) {
        obj.val(getRealVal(month) + getRealVal(day));
    }

    function getRealVal(val) {
        var result = val;
        if (parseInt(result) < 10) {
            result = "0" + result;
        }
        return result;
    }
});