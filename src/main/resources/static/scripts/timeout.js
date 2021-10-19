var idleTime = 0;

$(document).ready(function () {
    //Increment the idle time counter every minute.
    var idleInterval = setInterval(timerIncrement, 30000); // 30sec

    //Zero the idle timer on mouse movement.
    //$(this).mousemove(function (e) {
    //    idleTime = 0;
    //});
    $(this).keypress(function (e) {
        idleTime = 0;
    });
});

function timerIncrement() {
    idleTime = idleTime + 1;
    if (idleTime > 2 && goHome==1) { // 20 minutes
        location.href="/board/index.do?lang="+lang;
    }
}



/*
video.addEventListener('mousemove', () => {
    setOpacity(videoControl, 0.5);
    resetTimeOut();
}
 
);*/