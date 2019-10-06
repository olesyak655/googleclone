var main = function () {
    "use strict";
    $(".title").on("click", function (event) {
        var $url = $("a").attr('href')
        console.log("Это значение url " + $url);
        $(location).attr('href',$url);
    });
 };
 $(document).ready(main);