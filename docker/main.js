MINPLAYHEIGHT = 40

safari=0;
flipping=1;
noVUnits=0;
freshLoad = 1;
curAlbum = 1;
slideAlbum = 0;
curTrack = 1;
hoverTimer = 0;

setHeadVideo = 0;

stopped = 0;
stillUp= 1;
visRunning = 0;
playerShowing = 0;

barStyles = [];
styleNum = 0;
barStyle = "#d00";

capStyles = [];
capStyleNum = 0;
capStyle = "#f00";

tracksHtml = "";
trackInfoHtml = "";
rememberSlide = "";
rememberSlideB= "";
trackSharerOpen=0;

var pagePath = window.location.pathname.substr(window.location.pathname.lastIndexOf('/')+1);

musicShown=0;
var albumFound;

var context;
var audioStartNow=0;
webAudioYes=0;
checkWebAudio = function(){
    try {
        window.AudioContext = window.AudioContext||window.webkitAudioContext;
        context = new AudioContext();
        webAudioYes=1;
    }
        catch(e) {
        webAudioYes=0;
        console.log('Internet Explorer does not support the Web Audio API.');
    }  
}

if (navigator.userAgent.search("Safari") >= 0 && navigator.userAgent.search("Chrome") < 0) safari=1;
if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) var mobile=1;



var vCodes=[];
var bvCodes=[];

var videos = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "page/videos/videos.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 


for (var i = 0; i < videos.length; i++) {

    if (videos[i].tags.indexOf('music-video')>-1) {
        if (videos[i].tags.indexOf('coming-soon')>-1) {
            vCodes.push(videos[i].text);

        } else {
            vCodes.push(videos[i].youtube);
        }

    } else if (videos[i].tags.indexOf('gravity-tv')>-1) {
        if (videos[i].tags.indexOf('coming-soon')>-1) {
            bvCodes.push(videos[i].text);

        } else {
            bvCodes.push(videos[i].youtube);
        }
    }
};


var slideList = "",
    bSlideList= "",
    thumbList = [];

for (var i = 0; i < vCodes.length; i++) {
    if (vCodes[i].length==11) {
        slideList += '<a class="rsImg" href="http://i1.ytimg.com/vi/'+vCodes[i]+'/hqdefault.jpg" data-rsVideo="http://www.youtube.com/embed/'+vCodes[i]+'"></a>';
        
        thumbList.push('http://i1.ytimg.com/vi/'+vCodes[i]+'/1.jpg');
        thumbList.push('http://i1.ytimg.com/vi/'+vCodes[i]+'/2.jpg');
        thumbList.push('http://i1.ytimg.com/vi/'+vCodes[i]+'/3.jpg');
        
    } else {
        
        slideList+='<div class="rsBtnCenterer"><div class="gravityTv"><div>'+vCodes[i]+'</div></div></div>';
    };
};
for (var i = 0; i < bvCodes.length; i++) {
    if (bvCodes[i].length==11) {

        bSlideList+='<a class="rsImg" href="http://i1.ytimg.com/vi/'+bvCodes[i]+'/hqdefault.jpg" data-rsVideo="http://www.youtube.com/embed/'+bvCodes[i]+'"></a>';
        thumbList.push('http://i1.ytimg.com/vi/'+bvCodes[i]+'/1.jpg');
        thumbList.push('http://i1.ytimg.com/vi/'+bvCodes[i]+'/2.jpg');
        thumbList.push('http://i1.ytimg.com/vi/'+bvCodes[i]+'/3.jpg');


    } else {
        bSlideList+='<div class="rsBtnCenterer"><div class="gravityTv"><div>'+bvCodes[i]+'</div></div></div>';

    }
};




function analytics()
{
 (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-54360163-1', 'auto');
  ga('send', 'pageview');
}



function preload(arrayOfImages) {
    $(arrayOfImages).each(function(){
        $('<img/>')[0].src = this;
    });
}


var headlinesLeft = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "headlines-left.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 

var headlinesRight = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "headlines-right.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 



var albums = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "page/music/albums.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 

var albumList="";
var preloadList =[];
for (var i = 0; i < albums.length; i++) {
    albumList+='<div class="imgHolder"><a class="rsImg" href="albums/'+albums[i].folder+'/cover.jpg"></a></div>';
    preloadList.push('albums/'+albums[i].folder+'/cover.jpg');
    preloadList.push('albums/'+albums[i].folder+'/back-cover.jpg');
};



var gallerySlides = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "page/gallery/gallery.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 

var galleryList="";
for (var i = 0; i < gallerySlides.length; i++) {
    galleryList+='<a class="rsImg" href="img/slides/'+gallerySlides[i].url+'"><img class="rsTmb" src="img/slides/'+
                  gallerySlides[i].url.substr(0, gallerySlides[i].url.length-7)+'thum.jpg" /></a>';
};



var artists = (function () {
    var json = null;
    $.ajax({
        'async': false,
        'global': false,
        'url': "config/artists.json",
        'dataType': "json",
        'success': function (data) {
            json = data;
        }
    });
    return json;
})(); 



function GetURLParameter(sParam)   
{
    var sPageURL = (window.location.hash).replace("#!","");
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
};

function loadPage() 
{
    $('#navBar').children().each(function () {
        $(this).click(
            function() {
                history.pushState(this.id, "", this.id);    //  update history
                ga('send', 'pageview', {'page':this.id, 'title':this.id});
                loadSection(this.id);
            });
        $(this).hover(
            function() {
                TweenLite.to(this,.9,{alpha:.5});
            },
            function() {
                TweenLite.to(this,.9,{alpha:1});
            });
    })

    $('#title-words').click( function(){
        history.pushState("about", "", "about");    
        ga('send', 'pageview', {'page':"about", 'title':"about"});
        loadSection();        
    });
    
    if (!safari) {
        loadSection(pagePath);  
    };
    
    window.addEventListener('popstate', function(event){
        loadSection(pagePath);
    });
}
    
loadSection = function(navId) {
    if(!navId) {
        navId="about";
        document.title = "Gravity Academy";
    } else document.title = "Gravity Academy "+navId.charAt(0).toUpperCase()+navId.slice(1);

    // - * - * - * - * - * -
    if(musicShown) {
        $(window).unbind( "resize.vid");
    }
    // - * - * - * - * - * -

    function contentLoad() {

        $("#pageContent").removeClass();
        $("#pageContent").addClass(navId);

        $.get("page/" + navId + "/content.html", function (data) {
            $("#pageContent").html(data);
            TweenLite.to($("#contentBG"),1.4,{alpha:1,force3D:true,ease:Sine.easeIn});
            TweenLite.to($(".contentClass"),1.4,{alpha:1,force3D:true,ease:Sine.easeIn});
        });
    };

    TweenLite.to($("#contentBG"),.7,{alpha:0,force3D:true,ease:Sine.easeOut});
    TweenLite.to($(".fadeOuts"),.7,{alpha:0,delay:.6,force3D:true,ease:Sine.easeOut});
    TweenLite.to($(".contentClass"),.7,{alpha:0,delay:.6,force3D:true,ease:Sine.easeOut,onComplete:contentLoad});
}


function vUnitsHelper(){                    // for older stock android browser
    var ww = $(window).width();
    var wh = $(window).height();
    $("#title").css("font-size", ww*0.114);
    $("#title").css("word-spacing", ww*0.0182);
    $("#title-words").css("font-size", ww*0.05);
    $("#navBar").css("font-size", ww*0.046);
    //$("#contentWrap").css("font-size", ww*0.0125);


    $("#aboutBox").css("margin-left", ww*0.1);
    $("#aboutBox").css("font-size", ww*0.14);

    if ($('#pageContent').hasClass('artists')) {
        $("#artistArea").css("width", ww*0.96);
        $("#artistArea").css("margin-top", wh*0.08);
        $("#artistArea").css("margin-bottom", wh*0.08);
        $(".artistBox").css("border-width", ww*0.008);
        $("#TOKYOCIGAR").css("right", ww*0.633);
        $("#TOKYOCIGAR").css("top", ww*0.02);
        $("#TRIBEOFJUDAH").css("right", ww*0.307);
        $("#TRIBEOFJUDAH").css("top", ww*0.02);
        $("#TINATONER").css("right", ww*-0.02);
        $("#TINATONER").css("top", ww*0.02);
        $(".artistIMG").css("height", ww*0.2);
        $(".artistNameBox").css("margin-top", ww*0.235);
        $("#TOKYOCIGARname").css("left", ww*0.1);
        $("#TRIBEOFJUDAHname").css("left", ww*0.4);
        $("#TINATONERname").css("left", ww*0.75);
        $(".artistName").css("height", ww*0.025);
        $("#bioArea").css("height", ww*0.46);
        $(".bioBox").css("width", ww*0.5);
        $(".bioBox").css("left", ww*0.4);
        $(".bioBox").css("padding-top", ww*0.02);
        $(".bioBox").css("padding-right", ww*0.02);
        $(".bioBox").css("padding-left", ww*0.02);
        $(".bioBox").css("border-width", ww*0.008);
        $(".bioBox").css("margin-top", wh*0.15);
    };


    if ($('#pageContent').hasClass('music')) resizeMusic();

    if ($('#pageContent').hasClass('videos')) resizeVideo();

    if ($('#pageContent').hasClass('merch')) {
        $("#pageContent.merch .contentText").css("height", ww*0.86);
        $("#merchTopRow").css("width", ww*0.48);
        $(".itemBox").css("width", ww*0.2);
        $(".itemBox").css("margin", ww*0.02);
        $(".itemBox form").css("width", ww*0.2);
        $(".itemBox input").css("width", ww*0.2);
        $(".newRowStart").css("margin-left", ww*0.015);
    };


    if ($('#pageContent').hasClass('gallery')) {
        $("#galleryBox").css("width", ww*0.59);
        $("#galleryBox").css("margin-top", ww*0.02);
        $("#galleryBox").css("margin-right", ww*0.01);
        $("#galleryBox").css("margin-left", ww*0.01);
        $("#gallerySlider").css("width", ww*0.57);
        $("#gallerySlider").css("margin-top", ww*0.02);
        $("#gallerySlider").css("margin-right", ww*0.01);
        $("#gallerySlider").css("margin-bottom", ww*0.02);
        $("#gallerySlider").css("margin-left", ww*0.01);
        $("#gallerySlider").css("padding-top", ww*0.02);
        $("#gallerySlider").css("padding-right", ww*0.01);
        $("#gallerySlider").css("padding-bottom", ww*0.02);
        $("#gallerySlider").css("padding-left", ww*0.01);
        $(".rsDefault .rsThumbsHor").css("margin-top", ww*0.02);
        $(".rsDefault .rsThumbsHor").css("margin-bottom", ww*0.02);
    };


    if ($('#pageContent').hasClass('contact')) {
        $("#info").css("margin-right", ww*0.15);
        $("#info").css("margin-top", ww*0.12);
        $("#info").css("padding-top", ww*0.009);
        $("#info").css("padding-right", ww*0.017);
        $("#info").css("padding-bottom", ww*0.015);
        $("#info").css("padding-left", ww*0.017);

        $("#info").css("width", ww*0.3);
        $("#info").css("height", ww*0.2);

        $("#vBoxLeft").css("width", ww*0.43);
        $("#vBoxLeft").css("height", ww*0.46);
        $("#vBoxRight").css("width", ww*0.58);
        $("#vBoxRight").css("height", ww*0.46);
        $("#videoBgLeft").css("width", ww*0.53);
        $("#videoBgLeft").css("margin-left", ww*-0.02);
        $("#videoBgLeft").css("margin-top", ww*-0.01);
        $("#videoBgRight").css("width", ww*0.9);
        $("#videoBgRight").css("margin-left", ww*-0.21);
        $("#videoBgRight").css("margin-top", ww*-0.01);
    };

};

resizeMusic = function(){
    var ww = $(window).width();
    var wh = $(window).height();
    backHeight = wh-$('#contentWrap').offset().top;
    if (backHeight<ww*.6) backHeight=ww*.6;


    if ( ((backHeight*.9)-(ww*0.22))*2 < ww ) {

        $("#musicBox").css("height", ww*0.22);
        $("#musicSlider").css("width", ww*0.22);

        $('#musicBox').css("top",(backHeight*.96)-(ww*0.22));

        $('#audioRight').css("width",(backHeight*.9)-(ww*0.22) ); 
        $('#audioRight').css("height", (backHeight*.9)-(ww*0.22) );
        $('#audioRight').css("min-height", "none" );
        $('#audioRight').css("max-height", "none" );
        $('#audioRight').css("left",ww*.51); 

        $('.one, .two').css("right",ww*.51); 
        $('.one, .two').css("height", (backHeight*.9)-(ww*0.22) ); 
        $('.one, .two').css("width",(backHeight*.9)-(ww*0.22) ); 
 
        $(".one, .two").css("top", ww*0.01);    
        $("#audioRight").css("top", ww*0.01);
        $("#trackSharer").css("top", ww*0.01);

        $("#trackSharer").css("height", $("#audioRight").height()-ww*.023 );
        $("#trackSharer").css("padding", ww*.02);

    } else {    // portrait
        var a=ww*.55;
        var b=(backHeight*.9)-(ww*0.22);

        var space = backHeight-ww/2;
        $("#musicBox").css("height", ww*0.22);
        $("#musicSlider").css("width", ww*0.22);

        $('#musicBox').css("top",(a+b)/2);

        $('#audioRight').css("width",ww*.48 ); 
        $('#audioRight').css("height", "auto");
        $('#audioRight').css("min-height", ww*.48 );
        $('#audioRight').css("max-height", ((a+b)/2)-(ww*0.04) );
        $('#audioRight').css("left",ww*.51); 

        $('.one, .two').css("right",ww*.51); 
        $('.one, .two').css("height", ww*.49 ); 
        $('.one, .two').css("width", ww*.49 ); 

        $(".one, .two").css("top", wh*0.03);    
        $("#audioRight").css("top", wh*0.03);
        $("#trackSharer").css("top", wh*0.03);

        $("#trackSharer").css("height", $("#audioRight").height()-ww*.023 );
        $("#trackSharer").css("padding", ww*.02);

    }

    if (trackSharerOpen) $("#trackSharer").css("margin-left", ww*-0.029);
    else $("#trackSharer").css("margin-left", ww*-0.59);

    $("#audioRight").css("padding-top", ww*0.008);
    $("#audioRight").css("padding-right", ww*0.007);
    $("#audioRight").css("padding-bottom", ww*0.008);
    $("#audioRight").css("padding-left", ww*0.007);
    $("#musComBut").css("line-height", ww*0.017);
    $("#buyLink").css("margin-left", ww*0.005);
    $("#tracks").css("margin-top", ww*0.01);
    $("#tracks").css("width", ww*2);

    $('.shareLink').css("left", ($("#audioRight").width()-$(".shareLink").width()-(ww*0.007)) );

    $("#trackSharer").css("width", ww*0.5);
    $("#leftSlideIn").css("width", ww*0.5);
    $("#leftSlideIn").css("height", ww*0.55);
    $("#leftSlideIn").css("margin-left", ww*-0.52);
    $("#musicCommentsBox").css("width", ww*0.44);
    $("#musicCommentsBox").css("min-height", ww*0.23);
    $("#musicCommentsBox").css("margin", ww*0.01);
    $("#socialPluginsBox").css("margin-top", ww*0.01);
    $("#socialPluginsBox").css("margin-right", ww*0);

};

resizeVideo = function(){
    var ww = $(window).width();
    var wh = $(window).height();
    if (ww<1000) {
        var videoScaler = ww*(((1000-ww)*.001)+1);
    } else {
        var videoScaler = ww;
    }
    //console.log(ww+' - '+videoScaler);

    if (mobile) $("#controlButtons").css("display", "none");
    else {
        $("#controlButtons").css("width", ww*0.06);
        $("#controlButtons").css("height", ww*0.15);
        $("#controlButtons").css("left", ww-((ww-(videoScaler*0.36))*.33) );
        $("#controlButtons").css("top", (ww*-0.04)+wh*0.28 );
        
        $("#upButton").css("width", ww*0.03);
        $("#upButton").css("height", ww*0.03);
        $("#upButton").css("margin-top", ww*0.015);
        $("#upButton").css("margin-bottom", ww*0.015);
        $("#downButton").css("width", ww*0.03);
        $("#downButton").css("height", ww*0.03);
        $("#downButton").css("margin-top", ww*0.015);
        $("#downButton").css("margin-bottom", ww*0.015);

        $("#fullScreenButton").css("width", ww*0.03);
        $("#fullScreenButton").css("height", ww*0.03);
        $("#fullScreenButton").css("margin-top", ww*0.015);
        $("#fullScreenButton").css("margin-bottom", ww*0.015);
    };

    $(".videoButtons").css("font-size", ww*0.023);
    $(".videoButtons").css("padding", ww*0.018);

    $("#infoButton").css("left", ww-((ww-(videoScaler*0.36))*.33) );
    $("#infoButton").css("top", (ww*0.13)+wh*0.28 );

    $("#videoButton").css("left", ww-((ww-(videoScaler*0.36))*.33) );
    $("#videoButton").css("top", (ww*0.2)+wh*0.28 );

    /*$("#commentsButton").css("left", ww-((ww-(videoScaler*0.35))*.2) );
    $("#commentsButton").css("top", ww*0.38);*/
    
    
    if ($('#infoButton').hasClass('open')) $("#commentsBoxOuter").css("margin-left", videoScaler*-0.01);
    else $("#commentsBoxOuter").css("margin-left", videoScaler*-0.35);

    $("#commentsBoxOuter").css("width", videoScaler*0.31);
    $("#commentsBoxOuter").css("height", videoScaler*0.36);
    $("#commentsBoxOuter").css("margin-top", (videoScaler*-0.08)+wh*0.28 ); // ---
    $("#commentsBoxOuter").css("border-width", videoScaler*0.01);

    $("#commentsHeader").css("font-size", videoScaler*0.02);
    $("#commentsHeader").css("width", videoScaler*0.29);
    $("#commentsHeader").css("height", videoScaler*0.1);
    $("#commentsHeader").css("padding-left", videoScaler*0.01);
    $("#commentsHeader").css("padding-right", videoScaler*0.01);
    $(".comHeaderImage").css("width", videoScaler*0.086);
    $(".comHeaderImage").css("padding-top", videoScaler*0.001);
    $(".comHeaderImage").css("padding-right", videoScaler*0.005);
    $(".comHeaderImage").css("padding-bottom", videoScaler*0.001);
    $(".comHeaderImage").css("padding-left", videoScaler*0.005);
    $("#commentsBox").css("width", videoScaler*0.29);
    $("#commentsBox").css("min-height", videoScaler*0.25);
    $("#commentsBox").css("padding-top", videoScaler*0.01);
    $("#commentsBox").css("padding-right", videoScaler*0.01);
    $("#commentsBox").css("padding-left", videoScaler*0.01);

    $("#infoBox").css("font-size", videoScaler*0.014);
    $("#infoBox").css("width", videoScaler*0.29);
    $("#infoBox").css("height", videoScaler*0.24);
    $("#infoBox").css("top", videoScaler*0.11);
    $("#infoBox").css("padding-top", videoScaler*0.01);
    $("#infoBox").css("padding-right", videoScaler*0.01);
    $("#infoBox").css("padding-left", videoScaler*0.01);
    $(".fb_iframe_widget_fluid span iframe[style]").css("min-height", videoScaler*0.2);



    $("#bVideoSlider").css("width", videoScaler*0.36);
    $("#bVideoSlider").css("left", (ww-(videoScaler*0.36))/2); 
    $("#bVideoSlider").css("top", wh*0.28);

    $("#videoSlider").css("width", videoScaler*0.36);
    $("#videoSlider").css("left", (ww-(videoScaler*0.36))/2);
    $("#videoSlider").css("top", wh*0.28);

    $("div.rsBtnCenterer").css("width", videoScaler*0.5);
    $("div.rsBtnCenterer").css("left", videoScaler*-0.07);
    $("div.rsBtnCenterer").css("top", videoScaler*-0.01);
    $("div.rsBtnCenterer").css("height", videoScaler*0.24);

    $("#bVideoSlider.royalSlider.rsDefault.rsVer.rsFullscreen .rsBtnCenterer").css("left", videoScaler*0.25);
    $("#videoSlider.royalSlider.rsDefault.rsVer.rsFullscreen .rsBtnCenterer").css("left", videoScaler*0.25);
    $("#bVideoSlider.royalSlider.rsDefault.rsVer.rsFullscreen .rsBtnCenterer").css("top", videoScaler*0.25); // vh
    $("#videoSlider.royalSlider.rsDefault.rsVer.rsFullscreen .rsBtnCenterer").css("top", videoScaler*0.25); // vh

    $("#bVideoSlider div.rsPlayBtn").css("margin-left", videoScaler*0.22);
    $("#bVideoSlider div.rsPlayBtn").css("margin-top", videoScaler*0.1);
    $("#videoSlider div.rsPlayBtn").css("margin-left", videoScaler*0.22);
    $("#videoSlider div.rsPlayBtn").css("margin-top", videoScaler*0.1);
    $("div.gravityTv").css("width", videoScaler*0.36);
    $("div.gravityTv").css("height", videoScaler*0.23);
    $("div.gravityTv").css("margin-left", videoScaler*0.07);
    $(".gravityTv div").css("width", videoScaler*0.36);
    $(".gravityTv div").css("margin-top", videoScaler*0.14);
};

function setInfoSize(){    

    var ww = $(window).width();
    var wh = $(window).height();

    if (noVUnits) vUnitsHelper();

    playHeight = Math.floor(ww/25);

    if ($('#pageContent').hasClass('videos')) {
        resizeVideo();
        $('#musVis').css("height",playHeight);

    } else {
        if (playHeight<MINPLAYHEIGHT) playHeight = MINPLAYHEIGHT;

        $('#musVis').css("height",playHeight); 

        $("#playerInfo").css("width", ww*0.28);
        $("#playerInfo").css("height", playHeight);
        $("#playerInfo").css("right", ww*0.007);

        $('#playerInfo').css("top",$('#musVis').offset().top); 

        $('#playerInfo div').css("height",playHeight*.3); 
        $('#playerInfo div').css("right",playHeight*1.1); 
        $('#playerInfo div').css("font-size",playHeight*.26); 

        $('#infoTrack').css("top",playHeight*.07); 
        $('#infoArtist').css("top",playHeight*.37); 
        $('#infoAlbum').css("top",playHeight*.67); 

        $('#playerInfo img').css("width",playHeight*.86); 
        $('#playerInfo img').css("height",playHeight*.86); 
        $('#playerInfo img').css("margin-top",playHeight*.08); 

        $('#playerBox').css("top",$('#musVis').offset().top); 
        $('#playerBox').css("height",playHeight); 

        //console.log(playPos.left);

        $('.mejs-controls .mejs-button button').css("width",playHeight*.77); 
        $('.mejs-controls .mejs-button button').css("height",playHeight*.77); 
        $('.mejs-controls .mejs-button button').css("margin-top",playHeight*.1); 

        $(".mejs-controls div.mejs-time-rail").css("margin-left", ww*0.05);
        $(".mejs-controls div.mejs-time-rail").css("top", ww*0.01);
        $(".mejs-controls .mejs-time-rail span").css("height", ww*0.007);
        $(".mejs-controls .mejs-time-rail .mejs-time-total").css("width", ww*0.25);
        $(".mejs-controls .mejs-time-rail .mejs-time-handle").css("width", ww*0.005);
        $(".mejs-controls .mejs-time-rail .mejs-time-handle").css("height", ww*0.007);
        $(".mejs-controls .mejs-time-rail .mejs-time-float").css("top", ww*-0.014);

        $('div.mejs-time-rail').css("marginLeft",playHeight*1.4); 
        $('div.mejs-time-rail').css("top",playHeight*.41); 
        $('.mejs-controls .mejs-time-rail span').css("height",playHeight*.18); 
    };
    
    if ($('#pageContent').hasClass('merch')) {

        if (ww>940) {
            $('#pageContent').css("height", ww*.9);
        } else {
            $('#pageContent').css("height", ww*2.2);

            if (noVUnits) {
                $(".newRowStart").css("margin-left", ww*0.015);
                $("#merchTopRow").css("width", ww*0.8);
                $("#merchTopRow").css("height", ww*0.67);
                $(".itemBox").css("width", ww*0.36);
                $(".rowBreak").css("width", ww*0.8);
                $(".itemBox form").css("width", ww*0.36);
                $(".itemBox input").css("width", ww*0.36);
            };

        }

    } else if ($('#pageContent').hasClass('videos')) {

        backHeight = wh-$('#contentWrap').offset().top;
        if (backHeight<ww*.6) backHeight=ww*.6;

        $('#pageContent').css("height", backHeight);
        $('#contentBG').css("height", backHeight);
        

        if (backHeight*1.25>ww) {
            console.log('tall');
            $('#contentBG').css("width", backHeight*1.25);
            $('#contentBG').css("left", (((backHeight*1.25)-ww)/2)*-1 );

            $('#contentBGImg').css("height", backHeight);
            $('#contentBGImgCol').css("height", backHeight);

            $('#contentBGImg').css("width", "auto");
            $('#contentBGImgCol').css("width", "auto");

        } else {
            console.log('wide');
            $('#contentBG').css("width", ww);
            $('#contentBG').css("left", 0 );

            $('#contentBGImg').css("width", ww);
            $('#contentBGImgCol').css("width", ww);

            $('#contentBGImg').css("height", "auto");
            $('#contentBGImgCol').css("height", "auto");

        };

    } else {
        backHeight = wh-$('#contentWrap').offset().top;
        if (backHeight<ww*.6) backHeight=ww*.6;

        $('#pageContent').css("height", backHeight);
        $('#contentBG').css("height", backHeight);

    };
        //console.log(backHeight+' - '+ww);


    if ($('#pageContent').hasClass('music')) resizeMusic();


    console.log('player height set to '+playHeight);
    

}


//                                      88888888888888888888888888888888888888888
//                                      88888888888888888888888888888888888888888
//                                      888888                             888888
//                                      888888                             888888
//                                      888888                             888888
//                                      888888                             888888
//                                      888888                             888888
//                                      888888                             888888
//                                      888888                             888888
//                                      88888888888888888888888888888888888888888
//                                      88888888888888888888888888888888888888888
    
$(document).ready(function() {

    if ($('#vUnitTester').offset().top ==1) {
        console.log('viewport units not supported');
        noVUnits=1; 
    }
    //noVUnits=1;
    //if (noVUnits) vUnitsHelper();

    if (mobile||safari) flipping=0; 
    else flipping=1;


    if (pagePath=='artists') { 
        var loadList =[
            'img/artists/tok.jpg',
            'img/artists/toj.jpg',
            'img/artists/tin.jpg',
            'img/artists/tok-col.jpg',
            'img/artists/toj-col.jpg',
            'img/artists/tin-col.jpg',
            'img/artists/TOKYOCIGAR.png',
            'img/artists/TRIBEOFJUDAH.png',
            'img/artists/TINATONER.png', 
            'img/artists/contentbg.jpg'
            ];
    } else if(pagePath=='music'){
        var loadList= [
            'img/music/contentbg.jpg',
            'img/music/player/play-pause.png'
            ];
    } else if(pagePath=='videos'){
        var loadList= [
            'img/videos/contentbg.jpg',
            'img/videos/contentbgCol.jpg',
            'img/videos/film.png',
            'img/videos/arrowUp.png',
            'img/videos/arrowDown.png',
            'img/videos/fs256.png'
            ];
    } else if(pagePath=='merch'){
        var loadList= [
            'img/merch/contentbg.jpg'
            ];
    } else if(pagePath=='gallery'){
        var loadList= [
            'img/gallery/contentbg.jpg'
            ];
    } else if(pagePath=='contact'){
        var loadList= [
            'img/contact/contactLeftBG.jpg',
            'img/contact/contactLeftBGcol.jpg',
            'img/contact/contactRightBG.jpg',
            'img/contact/contactRightBGcol1.jpg',
            'img/contact/contactRightBGcol2.jpg'
            ];
    } else {
        var loadList= [
            'img/about/contentbg.jpg'
            ];
    };

    var loJackHtml='';
    var secondJackHtml='';

    for(var i=0; i < loadList.length; i++) { loJackHtml+='<img src="'+loadList[i]+'" width=1 height=1 />\n' };
    if(pagePath=='music') for(var i=0; i < preloadList.length; i++){loJackHtml+='<img src="'+preloadList[i]+'" width=1 height=1 />\n'};
    //if(pagePath=='videos') for(var i=0; i < thumbList.length; i++) { loJackHtml+='<img src="'+thumbList[i]+'" width=1 height=1 />\n' };
 
    //console.log(loJackHtml);
    $('#notHere').html(loJackHtml);


    var lazyList=[
        'img/artists/contentbg.jpg',
        'img/artists/tok.jpg',
        'img/artists/toj.jpg',
        'img/artists/tin.jpg',
        'img/artists/tok-col.jpg',
        'img/artists/toj-col.jpg',
        'img/artists/tin-col.jpg',
        'img/artists/TOKYOCIGAR.png',
        'img/artists/TRIBEOFJUDAH.png',
        'img/artists/TINATONER.png',
        'img/music/contentbg.jpg',
        'img/music/player/play-pause.png',
        'img/videos/contentbg.jpg',
        'img/videos/contentbgCol.jpg',
        'img/videos/film.png',
        'img/videos/arrowUp.png',
        'img/videos/arrowDown.png',
        'img/videos/fs256.png',
        'img/merch/contentbg.jpg',
        'img/gallery/contentbg.jpg',
        'img/contact/contactLeftBG.jpg',
        'img/contact/contactLeftBGcol.jpg',
        'img/contact/contactRightBG.jpg',
        'img/contact/contactRightBGcol1.jpg',
        'img/contact/contactRightBGcol2.jpg',
        'img/about/contentbg.jpg'
        ];
    for(var i=0; i < lazyList.length; i++) { secondJackHtml+='<img src="'+lazyList[i]+'" width=1 height=1 />\n' };
    for(var i=0; i < preloadList.length; i++){secondJackHtml+='<img src="'+preloadList[i]+'" width=1 height=1 />\n'};        
    var tur = setTimeout(function(){
        $('#notHere').html(secondJackHtml);
    },4000);


    analytics();

    TweenLite.to($("header"),.7,{alpha:1,delay:1,force3D:true,ease:Sine.easeIn});
    if (!mobile) TweenLite.to($(".headlines"),.7,{alpha:1,delay:1,force3D:true,ease:Sine.easeIn});

    if (GetURLParameter('a')) {
        var a = GetURLParameter('a'), albumFound=0;
        for (var i = 0; i < albums.length; i++) {
            if (albums[i].folder==a) {
                curAlbum = i;
                console.log('Album set to '+albums[i].title);
                albumFound = 1;
                audioStartNow=1;
            };
        };
        if (albumFound) {
            if (GetURLParameter('t')) {
                var t = Number(GetURLParameter('t'));
                if (t > 0 && t <= albums[curAlbum].tracks.length) {
                    curTrack = t;
                    console.log('track '+t);
                } else {
                    console.log('bad track value. ('+t+')');
                };
            };
        } else {
            console.log('album not found. ('+a+')');
        }
    };

    $('#playerInfo').html('<div id="infoTrack"></div><div id="infoArtist"></div>'+
                          '<div id="infoAlbum"></div><img src="albums/'+albums[curAlbum].folder+'/cover.jpg" />');
    $('#infoTrack').html(albums[curAlbum].tracks[curTrack-1].name);
    $('#infoArtist').html('by '+albums[curAlbum].artist);
    $('#infoAlbum').html('on '+albums[curAlbum].title);

    loadPage();
    
    if(!safari) checkWebAudio();

    if (webAudioYes) {

        var audio = new Audio();
        audio = document.getElementById('audio-player');

        source = context.createMediaElementSource(audio); 
        analyser = context.createAnalyser(); 
        source.connect(analyser);
        analyser.connect(context.destination);

        var canvas = document.getElementById('analyser_render');
        var cwidth,
            cheight,
            meterWidth,
            gap = 0,
            capHeight = 2,
            meterNum = 36,
            meterDisplayLength = 15,  // 10 is default
            valueHeight,
            padding,
            padRectSta = [],
            padRect = [],
            capYPositionArray = [];

            stopped = 1;
            statusBeeper = 0;
            visMode = 0;
            ctx = canvas.getContext('2d');

        function readOptions(){

            if (GetURLParameter('b')) {
                var b = Number(GetURLParameter('b'));
                if (b >= 0 && b < 101) {
                    meterNum = b;
                    console.log('bars value found:'+meterNum);
                } else {
                    console.log('bad bars value. ('+b+')');
                };
            };
            if (GetURLParameter('g')) {
                var g = Number(GetURLParameter('g'));
                if (g >= 0 && g < 51) {
                    gap = g;
                    console.log('gap value found:'+gap);
                } else {
                    console.log('bad gap value. ('+g+')');
                };
            };
            if (GetURLParameter('s')) {
                var s = Number(GetURLParameter('s'));
                if (s >= 0 && s < 10) {
                    styleNum = s;
                    console.log('style value found:'+s);
                } else {
                    console.log('bad style value. ('+s+')');
                };
            };
            if (GetURLParameter('c')) {
                var c = Number(GetURLParameter('c'));
                if (c >= 0 && c < 7) {
                    capStyleNum = c;
                    console.log('cap style value found:'+c);
                } else {
                    console.log('bad cap style value. ('+c+')');
                };
            };
            if (GetURLParameter('l')) {
                var l = Number(GetURLParameter('l'));
                if (l >= 0 && l < 30) {
                    meterDisplayLength = l;
                    console.log('length value found:'+l);
                } else {
                    console.log('bad length value. ('+l+')');
                };
            };

            

        };
        readOptions();

        setValues = function() {

                setInfoSize();

                var ww = $(window).width();
                cwidth = Math.floor(ww/2);
                cheight = Math.floor(ww/25);

                if (cheight<MINPLAYHEIGHT) cheight=MINPLAYHEIGHT;

                canvas.height = cheight;
                canvas.width = cwidth;
                
                valueHeight = (ww/2000)*(meterDisplayLength/10);

                if ((meterNum+(meterNum*gap)+gap) > cheight) {
                    if (meterNum>cheight){
                        gap=0;
                        meterNum=cheight;
                        console.log('Number of meters reduced to '+meterNum+'. (canvas height)');
                    } else {
                        console.log('Gap reduced to 0 for space. (it was '+gap+')');
                        gap=0;
                    };
                }

                meterWidth = Math.floor((cheight-gap)/meterNum)-gap;
                padding = (cheight-gap)%meterNum;
                padRectSta[0]=0;
                for (var i = 0; i < meterNum; i++) {
                    padRect[i] = 0;
                    if (i<padding) {
                        padRect[i] = 1;
                        padRectSta[i+1] = i+1;
                    } else {
                        padRectSta[i+1] = padRectSta[i];
                    }
                };

                console.log("Values set for "+meterNum+" meter(s).");
                console.log("Canvas Width:"+cwidth+" Height:"+cheight);
                console.log("Meter Width:"+meterWidth+" Gap:"+gap+" Padding:"+padding);

                gradient = ctx.createLinearGradient(0, 0, cwidth, 0);
                gradient.addColorStop(0.6, '#f00');
                gradient.addColorStop(0.5, '#000');
                gradient.addColorStop(0.4, '#f00');
                barStyles[0] = gradient;

                gradient2 = ctx.createLinearGradient(0, 0, cwidth, 0);
                gradient2.addColorStop(0.6, '#f00');
                gradient2.addColorStop(0.5, '#fff');
                gradient2.addColorStop(0.4, '#f00');
                barStyles[1] = gradient2;

                gradient3 = ctx.createLinearGradient(0, 0, cwidth, 0);
                gradient3.addColorStop(0.8, '#fa0');
                gradient3.addColorStop(0.6, '#f00');
                gradient3.addColorStop(0.5, '#000');
                gradient3.addColorStop(0.4, '#f00');
                gradient3.addColorStop(0.2, '#fa0');
                barStyles[2] = gradient3;

                gradient4 = ctx.createLinearGradient(0, -200, cwidth, cheight+200);
                gradient4.addColorStop(0.6, '#f00');
                gradient4.addColorStop(0.5, '#000');
                gradient4.addColorStop(0.4, '#f00');
                barStyles[3] = gradient4;

                gradient5 = ctx.createLinearGradient(0, -200, cwidth, cheight+200);
                gradient5.addColorStop(0.6, '#f00');
                gradient5.addColorStop(0.5, '#fff');
                gradient5.addColorStop(0.4, '#f00');
                barStyles[4] = gradient5;

                gradient6 = ctx.createLinearGradient(0, -200, cwidth, cheight+200);
                gradient6.addColorStop(0.8, '#fa0');
                gradient6.addColorStop(0.6, '#f00');
                gradient6.addColorStop(0.5, '#000');
                gradient6.addColorStop(0.4, '#f00');
                gradient6.addColorStop(0.2, '#fa0');
                barStyles[5] = gradient6;

                barStyles[6] = "#e00";
                barStyles[7] = "#000";
                barStyles[8] = "#fff";

                function drawPattern(img) {
                     var tempCanvas=document.createElement("canvas"),tCtx = tempCanvas.getContext("2d");
                     tempCanvas.width = cwidth;
                     tempCanvas.height = cheight;
                     tCtx.drawImage(img, 0, 0, cwidth, cheight);
                     barStyles[9] = ctx.createPattern(tempCanvas, 'repeat');
                };
                var img = new Image();
                img.src = "img/music/pattern1.jpg";
                img.onload = function(){
                    drawPattern(this);
                };

                capStyles[0] = "#f00";
                capStyles[1] = "#fff";
                capStyles[2] = "#000";
                capStyles[3] = "#555";
                capStyles[4] = "#ff0";
                capStyles[5] = "#0d0";

                capGradient1 = ctx.createLinearGradient(0, 0, cwidth, 0);
                capGradient1.addColorStop(0.8, '#f00');
                capGradient1.addColorStop(0.55, '#f00');
                capGradient1.addColorStop(0.5, '#fff');
                capGradient1.addColorStop(0.45, '#f00');
                capGradient1.addColorStop(0.2, '#f00');
                capStyles[6] = capGradient1;

                capStyle = capStyles[capStyleNum];
                barStyle = barStyles[styleNum];
        };

        setValues();

        if(visMode==1) {
            var frameLoop = function() {    // coming soon
            }; 

        } else {
            var frameLoop = function() {
                visRunning=1;

                if ((stopped)&&(stillUp==0)) { visRunning=0; return; }

                //statusBeeper++; if(statusBeeper>59){statusBeeper=0; console.log('vis running - -');};

                window.requestAnimationFrame(frameLoop);
                fbc_array = new Uint8Array(analyser.frequencyBinCount);
                analyser.getByteFrequencyData(fbc_array);
                var step = Math.round(fbc_array.length / meterNum);
                ctx.clearRect(0, 0, canvas.width, canvas.height); 
                x = Math.round(cwidth/2);

                if (stopped) stillUp=0; else stillUp=1;
                
                for (var i = 0; i < meterNum; i++) {

                    var value = (fbc_array[i * step]) * valueHeight;
                    y = gap + padRectSta[i] + i * (meterWidth + gap);
                    ctx.fillStyle = capStyle;

                    if (stopped) {
                        if (capYPositionArray[i]>0) {
                            stillUp = 1; 
                            ctx.fillRect( x - ( --capYPositionArray[i] ) - capHeight, y, capHeight, meterWidth+padRect[i]);
                            ctx.fillRect( x + ( capYPositionArray[i] ), y, capHeight, meterWidth+padRect[i]);
                        }
                    } else {

                        if (value < capYPositionArray[i]) {
                            ctx.fillRect( x - ( --capYPositionArray[i] ) - capHeight, y, capHeight, meterWidth+padRect[i]);
                            ctx.fillRect( x + ( capYPositionArray[i] ), y, capHeight, meterWidth+padRect[i]);
                        } else {
                            if (capYPositionArray[i]>0) {
                                ctx.fillRect( x - value - capHeight, y, capHeight, meterWidth+padRect[i]);
                                ctx.fillRect( x + value, y, capHeight, meterWidth+padRect[i]);
                            };
                            capYPositionArray[i] = value;
                        };

                        ctx.fillStyle = barStyle;        
                        ctx.fillRect( x, y, -1 * value, meterWidth+padRect[i] );
                        ctx.fillRect( x, y, value, meterWidth+padRect[i] );
                    };
                };
            };
        };




        $(document).keydown(function(e) {
            switch(e.which) {
                case 84: // t - more bars
                if ( (meterNum+1) + ((meterNum+1)*gap) + gap <= cheight) {
                    meterNum++;
                    meterWidth = Math.floor((cheight-gap)/meterNum)-gap;
                    padding = (cheight-gap)%meterNum;
                    padRectSta[0]=0;
                    for (var i = 0; i < meterNum; i++) {
                        padRect[i] = 0;
                        if (i<padding) {
                            padRect[i] = 1;
                            padRectSta[i+1] = i+1;
                        } else {
                            padRectSta[i+1] = padRectSta[i];
                        }
                    };
                    console.log('bars increased to '+meterNum);
                } else {
                    if (gap>0) console.log('no space. use "g" to change gap first.');
                    else console.log('Limit reached. One bar per (device independent) pixel.');
                }
                break;

                case 82: // r - less bars
                if (meterNum>1) {
                    meterNum--;
                    console.log('bars reduced to '+meterNum);
                    meterWidth = Math.floor((cheight-gap)/meterNum)-gap;
                    padding = (cheight-gap)%meterNum;
                    padRectSta[0]=0;
                    for (var i = 0; i < meterNum; i++) {
                        padRect[i] = 0;
                        if (i<padding) {
                            padRect[i] = 1;
                            padRectSta[i+1] = i+1;
                        } else {
                            padRectSta[i+1] = padRectSta[i];
                        }
                    };
                };
                break;

                case 71: // g is for gap
                if (meterNum + (meterNum*(gap+1))+(gap+1) <= cheight) {
                    gap++;
                    console.log('gap increased to '+gap);
                    meterWidth = Math.floor((cheight-gap)/meterNum)-gap;
                    padding = (cheight-gap)%meterNum;
                    padRectSta[0]=0;
                    for (var i = 0; i < meterNum; i++) {
                        padRect[i] = 0;
                        if (i<padding) {
                            padRect[i] = 1;
                            padRectSta[i+1] = i+1;
                        } else {
                            padRectSta[i+1] = padRectSta[i];
                        }
                    };
                } else {
                    if (gap!=0) {
                        gap = 0;
                        console.log('Gap set to zero.');
                        meterWidth = Math.floor((cheight-gap)/meterNum)-gap;
                        padding = (cheight-gap)%meterNum;
                        padRectSta[0]=0;
                        for (var i = 0; i < meterNum; i++) {
                            padRect[i] = 0;
                            if (i<padding) {
                                padRect[i] = 1;
                                padRectSta[i+1] = i+1;
                            } else {
                                padRectSta[i+1] = padRectSta[i];
                            }
                        };
                    } else console.log('not enough space to increase gap. use "r" to reduce bars first.');
                }
                break;

                case 83: // s is for style
                if (styleNum<9) {
                    styleNum++;
                } else {
                    styleNum=0;
                }
                barStyle = barStyles[styleNum];
                console.log('style '+styleNum);
                break;

                case 67: // c is for cap
                if (capStyleNum<6) {
                    capStyleNum++;
                } else {
                    capStyleNum=0;
                }
                capStyle = capStyles[capStyleNum];
                console.log('cap style '+capStyleNum);
                break;

                case 76: // l increases length
                if (meterDisplayLength<29) {
                    meterDisplayLength++;
                    console.log('length:'+meterDisplayLength);
                    valueHeight = (cwidth/1000)*(meterDisplayLength/10);
                };
                break;

                case 75: // k decreases length
                if (meterDisplayLength>0) {
                    meterDisplayLength--;
                    console.log('length:'+meterDisplayLength);
                    valueHeight = (cwidth/1000)*(meterDisplayLength/10);
                };
                break;

                case 85: // u is for url
                console.log('-------------- urls for current... ------------------');
                console.log('VISUAL');
                console.log(window.location.host+'/music#!b='+meterNum+'&g='+gap+'&s='+styleNum+'&c='+capStyleNum+'&l='+meterDisplayLength);
                console.log('TRACK');
                console.log(window.location.host+'/music#!a='+albums[curAlbum].folder+'&t='+curTrack);
                console.log('TRACK and VISUAL');
                console.log(window.location.host+'/music#!a='+albums[curAlbum].folder+'&t='+curTrack+'&b='+meterNum+'&g='+gap+'&s='+styleNum+'&c='+capStyleNum+'&l='+meterDisplayLength);
                console.log('ALBUM');
                console.log(window.location.host+'/music#!a='+albums[curAlbum].folder);
                console.log('ALBUM and VISUAL');
                console.log(window.location.host+'/music#!a='+albums[curAlbum].folder+'&b='+meterNum+'&g='+gap+'&s='+styleNum+'&c='+capStyleNum+'&l='+meterDisplayLength);
                break;

                default: return; // exit this handler for other keys
            }
            e.preventDefault(); // prevent the default action (scroll / move caret)
        });



 
    } else {

        setValues = function(){
            setInfoSize();
        }
        setValues();
    };





    hoverTime = function(wait){
        if (!mobile) {
            clearTimeout(hoverTimer);
            hoverTimer = setTimeout(function() {
                TweenLite.to($('.playerClass'),1.6,{alpha:0});
                playerShowing=0;
            }, wait);
        };
    };


    $('.playerClass').hover(
        function() {
            clearTimeout(hoverTimer);
            playerShowing=1;
            TweenLite.to($('.playerClass'),.9,{alpha:1});
        },
        function() {
            hoverTime(3000);
        }
    );





    player = new MediaElementPlayer('#audio-player',{
            alwaysShowControls: true,
            features: ['playpause','progress'],
            audioWidth: "100%",
            audioHeight: "100%",
            enableAutosize: true,
            success: function(mediaElement, domObject){
                mediaElement.addEventListener('ended',nextTrack, false);
                mediaElement.addEventListener('play', onPlay, false);
                mediaElement.addEventListener('pause',onPause, false);
            }
    });

    function onPlay(){
        if (webAudioYes) {
            if (stopped) {
                if (visRunning) {
                    stopped=0;
                } else {
                    stopped=0;
                    frameLoop();
                };
            };
        };

        $('#infoTrack').html(albums[curAlbum].tracks[curTrack-1].name);
        $('#infoArtist').html('by '+albums[curAlbum].artist);
        $('#infoAlbum').html('on '+albums[curAlbum].title);
        $("#playerInfo>img").attr('src','albums/'+albums[curAlbum].folder+'/cover.jpg');

        if (!playerShowing) {
            TweenLite.to($('.playerClass'),.9,{alpha:1});
            hoverTime(7000);
        };
    };

    function onPause(){
        stopped=1;
    }

    function nextTrack(mediaElement, domObject){ 
        if (albums[curAlbum].tracks[curTrack]) {
            curTrack++;
            if (slideAlbum==curAlbum) {
                TweenLite.to($( "ul li:nth-child("+(curTrack-1)+")" ),.5,{color:"#000"});
                TweenLite.to($( "ul li:nth-child("+(curTrack)+")" ),.5,{color:"#d00"});
            };

            player.setSrc([
               { src:'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3', type:'audio/mp3' }
            ]);
            if (noVUnits) $('audio').attr("src",'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
            player.play();

        };
    };

    
    var st = setTimeout(function() {
        player.setSrc([
           { src:'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3', type:'audio/mp3' }
        ]);
        if (noVUnits) $('audio').attr("src",'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
        setInfoSize();
        console.log('Track set to '+'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
    }, 1400);



    $( window ).resize(function() {
        console.log('-=- resizing -=-')
        setValues();

    });


if (audioStartNow) {
    console.log('autostart');
    var ggg = setTimeout(function() {
        console.log('autostart');
        player.play();
    },2000);
};



//                      88888           88888
//                      88888           88888
//                      88888           88888
//                      88888           88888
//                      88888           88888
//                      888888888888888888888
//                      888888888888888888888
//                      88888           88888
//                      88888           88888
//                      88888           88888
//                      88888           88888


    if(mobile) {
    
    } else {


        var leftText = [];
        var build='<div class="bline"></div>';
        for (var i = 0; i < 22; i++) {
            leftText[i]=36;
            build+='<div id="left'+i+'" class="letterBox" >'+
                   '<div id="leftB'+i+'" class="letter" > </div>'+
                   '<div id="leftA'+i+'" class="letter" > </div></div>';
        };
        $('#headlineLeft').html(build);

        var rightText = [];
        var buildr='<div class="bline"></div>';
        for (var i = 0; i < 22; i++) {
            rightText[i]=36;
            buildr+='<div id="right'+i+'" class="letterBox" >'+
                   '<div id="rightB'+i+'" class="letter" > </div>'+
                   '<div id="rightA'+i+'" class="letter" > </div></div>';
        };
        $('#headlineRight').html(buildr);



        var curLeft;
        var leftHead = function(headline) {
            curLeft = headline;
            for (var i = 0; i < 22; i++) {
                letterFlipLeft(i,headlinesLeft[curLeft].headline.toUpperCase().charCodeAt(i));
            };
        };

        curRight="";
        var rightHead = function(headline) {
            curRight = headline;
            for (var i = 0; i < 22; i++) {
                letterFlipRight(i,headlinesRight[curRight].headline.toUpperCase().charCodeAt(i));
            };
        };


        var letterFlipLeft = function(pos,letter){

            if (letter>47 && letter < 58) var letPos= letter-48;
            else if (letter>64 && letter < 91) var letPos= letter-55;
            else var letPos = 36;

            var counter = leftText[pos];

            if (counter!=letPos) {

                if (counter<=letPos) var numOfSteps = letPos-counter;
                else var numOfSteps = (37-counter)+letPos;
                //console.log(numOfSteps);

                var otherCounter = 0;
                var loop= function(){
                    //console.log(counter+' - '+letPos);

                    if (counter==letPos) {
                        leftText[pos] = counter;
                    } else {

                        otherCounter++;
                        if (counter<36) counter++; else counter=0;

                        if (counter<10) var newVal = String.fromCharCode(counter+48);
                        else if (counter==36) var newVal = " ";
                        else var newVal = String.fromCharCode(counter+55);

                        if (numOfSteps-otherCounter>7) var waitTime = 62;
                        else if (numOfSteps-otherCounter>5) var waitTime = 110;
                        else if (numOfSteps-otherCounter>3) var waitTime = 160;
                        else if (numOfSteps-otherCounter>1) var waitTime = 200;
                        else if (numOfSteps-otherCounter>0) var waitTime = 250;
                        else var waitTime =320;

                        $('#leftB'+pos).html(newVal);

                        function letSwitch(){
                            $('#leftA'+pos).html(newVal);
                            TweenMax.set($('#leftA'+pos),{height:"1.7vw"});
                        };
                        var a=$('#leftA'+pos);
                        var b=(waitTime/1400);
                        TweenMax.to(a,b,{height:0,onComplete:letSwitch});

                        var blink = setTimeout(loop,waitTime);
                    };

                };
                loop();
            };
        };

        var letterFlipRight = function(pos,letter){

            if (letter>47 && letter < 58) var letPos= letter-48;
            else if (letter>64 && letter < 91) var letPos= letter-55;
            else var letPos = 36;

            var counter = rightText[pos];        // --------------------------

            if (counter!=letPos) {

                if (counter<=letPos) var numOfSteps = letPos-counter;
                else var numOfSteps = (37-counter)+letPos;
                //console.log(numOfSteps);

                var otherCounter = 0;
                var loop= function(){
                    //console.log(counter+' - '+letPos);

                    if (counter==letPos) {
                        rightText[pos] = counter;        // --------------------------
                    } else {

                        otherCounter++;
                        if (counter<36) counter++; else counter=0;

                        if (counter<10) var newVal = String.fromCharCode(counter+48);
                        else if (counter==36) var newVal = " ";
                        else var newVal = String.fromCharCode(counter+55);

                        if (numOfSteps-otherCounter>7) var waitTime = 62;
                        else if (numOfSteps-otherCounter>5) var waitTime = 110;
                        else if (numOfSteps-otherCounter>3) var waitTime = 160;
                        else if (numOfSteps-otherCounter>1) var waitTime = 200;
                        else if (numOfSteps-otherCounter>0) var waitTime = 250;
                        else var waitTime =320;

                        $('#rightB'+pos).html(newVal);        // --------------------------

                        function letSwitch(){
                            $('#rightA'+pos).html(newVal);        // --------------------------
                            TweenMax.set($('#rightA'+pos),{height:"1.7vw"});        // --------------------------
                        };
                        var a=$('#rightA'+pos);        // --------------------------
                        var b=(waitTime/1400);
                        TweenMax.to(a,b,{height:0,onComplete:letSwitch});

                        var blink = setTimeout(loop,waitTime);
                    };

                };
                loop();
            };
        };

        var leftIndex=-1;
        function headlineLoopLeft() {
            leftIndex++;
            if (leftIndex>=headlinesLeft.length) leftIndex=0;
            var ha = setTimeout( function(){ leftHead(leftIndex); },3000);
            var hd = setTimeout( headlineLoopLeft,14000);
        };
        headlineLoopLeft();

        var rightIndex=-1;
        function headlineLoopRight() {
            rightIndex++;
            if (rightIndex>=headlinesRight.length) rightIndex=0;
            var hr = setTimeout( function(){ rightHead(rightIndex); },2500);
            var he = setTimeout( headlineLoopRight,7000);
        };
        headlineLoopRight();
        


        $('#headlineLeft').click(function(){
            if (headlinesLeft[curLeft].album) {
                //console.log('music');
                
                player.pause();
                if (slideAlbum==curAlbum) TweenLite.to($( "ul li:nth-child("+(curTrack)+")" ),.5,{color:"#000"});
                hoverTime(2000);

                var a = headlinesLeft[curLeft].album, albumFound=0;
                for (var i = 0; i < albums.length; i++) {
                    if (albums[i].folder==a) {
                        curAlbum = i;
                        console.log('Album set to '+albums[i].title);
                        albumFound = 1;
                    };
                };
                if (albumFound) {
                    if (headlinesLeft[curLeft].track) {
                        var t = Number(headlinesLeft[curLeft].track);
                        if (t > 0 && t <= albums[curAlbum].tracks.length) {
                            curTrack = t;
                            console.log('track '+t);
                        } else {
                            console.log('track bad or missing. set to track 1.');
                            curTrack = 0;
                        };
                    };

                    player.setSrc([
                       { src:'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3', type:'audio/mp3' }
                    ]);
                    if (noVUnits) $('audio').attr("src",'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
                    console.log('Track set to '+'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
                    player.play();

                } else {
                    console.log('album not found. ('+a+')');
                }


                if ($("#pageContent").hasClass('music')) {
                    //console.log('already in music');
                    musicSlider.royalSlider('goTo', curAlbum);
                    TweenLite.set($( "ul li:nth-child("+(curTrack)+")" ),{color:"#d00"}); // 4if already on slide

                } else {
                    //console.log('moving to music');
                    if (slideAlbum==curAlbum) {
                        loadSection('music');
                    } else {
                        freshLoad=1;
                        loadSection('music');
                    }
                };

            } else if (headlinesLeft[curLeft].video) {    //888888888888888888888888888888888888888888

                player.pause();

                function goToVideo(){
                    clearTimeout(flipTime);
                    if (flipping) {

                        if (vCodes.indexOf(headlinesLeft[curLeft].video)>-1) {
                            rememberSlide = vCodes.indexOf(headlinesLeft[curLeft].video);
                            console.log("video set: "+headlinesLeft[curLeft].video+" slide: "+rememberSlide);

                            if ($('#videoButton').hasClass('flipped')) {

                                var tlVid = new TimelineMax({paused:true});
                                tlVid.to('#videoSlider', 1, {rotationY:0,ease:Sine.easeInOut})
                                     .to('#bVideoSlider', 1, {rotationY:180,ease:Sine.easeInOut},0)
                                     .to('#videoSlider', .1, {alpha:1,zIndex:200},.5)
                                     .to('#bVideoSlider', .1, {alpha:0,zIndex:0},.5);

                                flipTime =setTimeout(function() {
                                       $("#bVideoSlider").html('');
                                    }, 600);        
                                sliderBack.data('royalSlider').destroy();

                                tlVid.play();
                                $('#videoButton').removeClass('flipped');

                                $("#videoSlider").html('');
                                sliderFront = $('#videoSlider');
                                frontSlideshow();
                                $("#videoButton").html('Episodes');
                                if ($('#commentsButton').hasClass('open')) {
                                    $('#commentsButton').removeClass('open');
                                    $('#infoButton').addClass('open');
                                };
                                updateInfo();
                                sliderFront.data('royalSlider').playVideo();                
                            } else {
                                sliderFront.royalSlider('goTo', rememberSlide);
                                sliderFront.data('royalSlider').playVideo();                
                
                            };

                        } else if (bvCodes.indexOf(headlinesLeft[curLeft].video)>-1) {
                            rememberSlideB = bvCodes.indexOf(headlinesLeft[curLeft].video);

                            if ($('#videoButton').hasClass('flipped')) {
                                sliderBack.royalSlider('goTo', rememberSlideB);
                                sliderBack.data('royalSlider').playVideo();                

                            } else {
                                var tlCom = new TimelineMax({paused:true});
                                tlCom.to('#videoSlider', 1, {rotationY:180,ease:Sine.easeInOut})
                                     .to('#bVideoSlider', 1, {rotationY:0,ease:Sine.easeInOut},0)
                                     .to('#videoSlider', .1, {alpha:0,zIndex:0},.5)
                                     .to('#bVideoSlider', .1, {alpha:1,zIndex:200},.5);

                                flipTime =setTimeout(function() {
                                       $("#videoSlider").html('');
                                    }, 600);
                                sliderFront.data('royalSlider').destroy();

                                tlCom.play();
                                $('#videoButton').addClass('flipped');

                                $("#bVideoSlider").html('');
                                sliderBack = $('#bVideoSlider');
                                backSlideshow();
                                $("#videoButton").html('Music Videos');
                                if ($('#commentsButton').hasClass('open')) {
                                    $('#commentsButton').removeClass('open');
                                    $('#infoButton').addClass('open');
                                };
                                updateInfo('b');
                                sliderBack.data('royalSlider').playVideo();                
                            };

                            console.log("video set: "+headlinesLeft[curLeft].video+" (back)slide: "+rememberSlideB);
                        } else {
                            console.log("video not found.");
                        };
                    } else {

                        rememberSlide = vCodes.indexOf(headlinesRight[curLeft].video);
                        sliderFront.royalSlider('goTo', rememberLeft);
                        sliderFront.data('royalSlider').playVideo();                
                        console.log("video set: "+headlinesRight[curLeft].video+" slide: "+rememberSlide);
                    }


                }; // end goToVideo


                if ($("#pageContent").hasClass('videos')) {
                    console.log('on video');
                    goToVideo();
                } else {
                    setHeadVideo=headlinesLeft[curLeft].video;
                    history.pushState("videos", "", "videos");    //  update history
                    loadSection('videos');
                }

            } else if (headlinesLeft[curLeft].url) {    // external link
                window.location.href = headlinesLeft[curLeft].url;
            }
        });



        $('#headlineRight').click(function(){
            if (headlinesRight[curRight].album) {
                //console.log('music');
                
                player.pause();
                if (slideAlbum==curAlbum) TweenLite.to($( "ul li:nth-child("+curTrack+")" ),.5,{color:"#000"});
                hoverTime(2000);

                var a = headlinesRight[curRight].album, albumFound=0;
                for (var i = 0; i < albums.length; i++) {
                    if (albums[i].folder==a) {
                        curAlbum = i;
                        console.log('Album set to '+albums[i].title);
                        albumFound = 1;
                    };
                };
                if (albumFound) {
                    if (headlinesRight[curRight].track) {
                        var t = Number(headlinesRight[curRight].track);
                        if (t > 0 && t <= albums[curAlbum].tracks.length) {
                            curTrack = t;
                            console.log('track '+t);
                        } else {
                            console.log('track bad or missing. set to track 1.');
                            curTrack = 0;
                        };
                    };

                    player.setSrc([
                       { src:'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3', type:'audio/mp3' }
                    ]);
                    if (noVUnits) $('audio').attr("src",'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
                    console.log('Track set to '+'albums/'+albums[curAlbum].folder+'/'+curTrack+'.mp3');
                    player.play();

                } else {
                    console.log('album not found. ('+a+')');
                }



                if ($("#pageContent").hasClass('music')) {
                    //console.log('already in music');
                    musicSlider.royalSlider('goTo', curAlbum);
                    TweenLite.set($( "ul li:nth-child("+curTrack+")" ),{color:"#d00"}); // 4if already on slide
      
                } else {
                    //console.log('moving to music');
                    if (slideAlbum==curAlbum) {
                        loadSection('music');
                    } else {
                        freshLoad=1;
                        loadSection('music');
                    }
                };

            } else if (headlinesRight[curRight].video) {    //888888888888888888888888888888888888888888

                player.pause();

                function goToVideo(){
                    clearTimeout(flipTime);

                    if (flipping) {

                        if (vCodes.indexOf(headlinesRight[curRight].video)>-1) {
                            rememberSlide = vCodes.indexOf(headlinesRight[curRight].video);
                            console.log("video set: "+headlinesRight[curRight].video+" slide: "+rememberSlide);

                            if ($('#videoButton').hasClass('flipped')) {

                                var tlVid = new TimelineMax({paused:true});
                                tlVid.to('#videoSlider', 1, {rotationY:0,ease:Sine.easeInOut})
                                     .to('#bVideoSlider', 1, {rotationY:180,ease:Sine.easeInOut},0)
                                     .to('#videoSlider', .1, {alpha:1,zIndex:200},.5)
                                     .to('#bVideoSlider', .1, {alpha:0,zIndex:0},.5);

                                flipTime =setTimeout(function() {
                                       $("#bVideoSlider").html('');
                                    }, 600);        
                                sliderBack.data('royalSlider').destroy();

                                tlVid.play();
                                $('#videoButton').removeClass('flipped');

                                $("#videoSlider").html('');
                                sliderFront = $('#videoSlider');
                                frontSlideshow();
                                resizeVideo();
                                $("#videoButton").html('Episodes');
                                if ($('#commentsButton').hasClass('open')) {
                                    $('#commentsButton').removeClass('open');
                                    $('#infoButton').addClass('open');
                                };
                                updateInfo();

                                sliderFront.data('royalSlider').playVideo();                

                            } else {
                                sliderFront.royalSlider('goTo', rememberSlide);

                                sliderFront.data('royalSlider').playVideo();                

                            };

                        } else if (bvCodes.indexOf(headlinesRight[curRight].video)>-1) {
                            rememberSlideB = bvCodes.indexOf(headlinesRight[curRight].video);

                            if ($('#videoButton').hasClass('flipped')) {
                                sliderBack.royalSlider('goTo', rememberSlideB);
                                sliderBack.data('royalSlider').playVideo();                

                            } else {
                                var tlCom = new TimelineMax({paused:true});
                                tlCom.to('#videoSlider', 1, {rotationY:180,ease:Sine.easeInOut})
                                     .to('#bVideoSlider', 1, {rotationY:0,ease:Sine.easeInOut},0)
                                     .to('#videoSlider', .1, {alpha:0,zIndex:0},.5)
                                     .to('#bVideoSlider', .1, {alpha:1,zIndex:200},.5);

                                flipTime =setTimeout(function() {
                                       $("#videoSlider").html('');
                                    }, 600);
                                sliderFront.data('royalSlider').destroy();

                                tlCom.play();
                                $('#videoButton').addClass('flipped');

                                $("#bVideoSlider").html('');
                                sliderBack = $('#bVideoSlider');
                                backSlideshow();
                                resizeVideo();
                                $("#videoButton").html('Music Videos');
                                if ($('#commentsButton').hasClass('open')) {
                                    $('#commentsButton').removeClass('open');
                                    $('#infoButton').addClass('open');
                                };
                                updateInfo('b');
                                if (noVUnits) vUnitsHelper();
                                sliderBack.data('royalSlider').playVideo();                
                            };

                            console.log("video set: "+headlinesRight[curRight].video+" (back)slide: "+rememberSlideB);
                        } else {
                            console.log("video not found.");
                        };

                    } else {

                        rememberSlide = vCodes.indexOf(headlinesRight[curRight].video);
                        sliderFront.royalSlider('goTo', rememberSlide);
                        sliderFront.data('royalSlider').playVideo();                
                        console.log("video set: "+headlinesRight[curRight].video+" slide: "+rememberSlide);
                    }
                }; // end goToVideo


                if ($("#pageContent").hasClass('videos')) {
                    console.log('on video');
                    goToVideo();
                } else {
                    setHeadVideo=headlinesRight[curRight].video;
                    history.pushState("videos", "", "videos");    //  update history
                    loadSection('videos');
                }

            } else if (headlinesRight[curRight].url) {    // external link
                window.location.href = headlinesRight[curRight].url;
            }
            
        });
    };



}); // end onload
