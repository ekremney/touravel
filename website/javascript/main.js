/**
  * Mobile Navigation
  * Top Slider
  * Retina Logos
  * Responsive Texts
  * Project Isotope
  * Blog Masonry
  * Tesimonials
  * Orches Slider
  * Toggles
  * Google Map
  * Header Sticky
  * Progress Bars
  * Parallax
*/

;(function($) {

   'use strict'

   var init_header = function() {
      var largeScreen = matchMedia('only screen and (min-width: 992px)').matches;
      if ( largeScreen ) {
         if( $().sticky ){
            $('header.header-sticky').sticky();
         }
      }

      $(window).scroll( function() {
         if ( $( window).scrollTop() > 200 ) {
            $('header').addClass('float-header');
         } else {
            $('header').removeClass('float-header');
         }
      });

      $('.one-page .mainnav ul > li > a').on('click',function() {
         var anchor = $(this).attr('href').split('#')[1];

         if (anchor) {
            if ( $('#'+anchor).length > 0 ) {
               var headerHeight = 0;
               if ( $('.header-sticky').length > 0 && largeScreen ) {
                  headerHeight = $('#header').outerHeight();
               }
               var target = $('#'+anchor).offset().top - headerHeight;
               $('html,body').animate({scrollTop: target}, 1000, 'easeInOutExpo');
            }
         }
         return false;
      }); // click on one-page menu
      
      $('.one-page .mainnav > ul > li > a').on( 'click', function() {
         $( this ).addClass('active').parent().siblings().children().removeClass('active');
      });
   };

   var fullScreen = function() {
      (function() {
         function setupSlider() {
            var sliderOrches = $('#top-slider'),
            headerHeight = $('#header').height(),
            windowHeight = $(window).height(),
            sliderHeight = 650;

            if ( $('body').hasClass('one-page') ){
               sliderHeight = windowHeight;
            }

            sliderOrches.css({ height: sliderHeight+"px", });
            var sliderContent = $('#top-slider .content'),
            contentHeight = sliderContent.height(),
            contentMargin = (sliderHeight - contentHeight) / 2;

            sliderContent.css({ 
               "margin-bottom" : contentMargin + "px",
               "margin-top": contentMargin - headerHeight + "px"
            });
         }

         $(window).on("resize", setupSlider);
         $(document).on("ready", setupSlider);
      })(); // set fullscreen and vertical align for content

      (function() {
         var current = 1; 
         var height = $('.text-scroll').height(); 
         var numberDivs = $('.text-scroll').children().length; 
         var first = $('.text-scroll h1:nth-child(1)');

         setInterval(function() {
            var number = current * -height;
            first.css('margin-top', number + 'px');
            if (current === numberDivs) {
              first.css('margin-top', '0px');
              current = 1;
            } else current++;
         }, 2500);
      })(); // scroll divs

      (function() {
         $('.btn-top').on('click',function() {
            var anchor = $(this).attr('href').split('#')[1];
            if (anchor) {
               if ( $('#'+anchor).length > 0 ) {
                  var headerHeight = 0;
                  if ( $('.header-sticky').length > 0 ) {
                     headerHeight = $('#header').outerHeight();
                  }
                  var target = $('#'+anchor).offset().top - headerHeight;

                  $('html,body').animate({scrollTop: target}, 1000, 'easeInOutExpo');
               }
            }
            return false;
         });
      })(); // scroll target
   };

   var retinaLogos = function() {
      var retina = window.devicePixelRatio > 1 ? true : false;

      if(retina) {
         $('.logo img').attr({src:'./images/logo@2x.png',width:'105',height:'23'});
      }
   };

   var responsiveTexts = function() {
      if ( $().fitText ){
         $(".text-scroll h1").fitText(1.3, { minFontSize: '20px', maxFontSize: '80px' });
      }
   };

   var projectIsotope = function() {
      if ( $().isotope ) {
         var $container = $('.project-container');

         $container.imagesLoaded(function(){
            $container.isotope({
               itemSelector: '.project-item',
               transitionDuration: '0.6s'
            }); // isotope
         });

         $('.project-filter li').on('click',function(){
            $('.project-filter li').removeClass('active');
            $(this).addClass('active');
            var selector = $(this).find("a").attr('data-filter');
            $container.isotope({ filter: selector });
            return false;
         }); // filter
      };
   };

   var blogMasonry = function() {
      if ( $().isotope ) {
         var $container = $('.posts-container');

         $container.imagesLoaded(function(){
            $container.isotope({
               itemSelector: '.post-item',
               transitionDuration: '0.5s',
               layoutMode: 'masonry',
               masonry: { columnWidth: $container.width() / 12 }
            }); // isotope
         });

         $(window).resize(function() {
            $container.isotope({
               masonry: { columnWidth: $container.width() / 12 }
            });
         }); // relayout
         
         $('.post-filter li').on('click',function(){
            $('.post-filter li').removeClass('active');
            $(this).addClass('active');
            var selector = $(this).find("a").attr('data-filter');
            $container.isotope({ filter: selector });
            return false;
         }); // filter
      };
   };

   var testimonial = function () {
      $('.testimonial-text').bxSlider({
         mode: 'fade',
         touchEnabled: true,
         oneToOneTouch: true,
         pagerCustom: '#bx-pager',
         nextSelector: '#bx-next',
         prevSelector: '#bx-prev',
         nextText: '<i class="icons-angle-right"></i>',
         prevText: '<i class="icons-angle-left"></i>',
      });
   };

   var orSlider = function() {
      if ( $().flexslider ) {
         $('.or-slider').each(function() {
            var $this = $(this);
            var easing = ( $this.data('effect') == 'fade' ) ? 'linear' : 'easeInOutExpo';
            $this.find('.flexslider').flexslider({
               animation      :  $this.data('effect'),
               direction      :  $this.data('direction'), // vertical
               pauseOnHover   :  true,
               useCSS         :  false,
               easing         :  easing,
               animationSpeed :  500,
               slideshowSpeed :  5000,
               controlNav     :  false,
               directionNav   :  true,
               slideshow      :  $this.data('auto'),
               prevText    :  '<i class="icons-angle-left"></i>',
               nextText    :  '<i class="icons-angle-right"></i>',
               smoothHeight   :  true,
            }); // flexslider
         }); // or-slider each
      }
   };

   var toggles = function() {
      var args = {easing : 'easeOutExpo', duration: 600};
      $('.toggle .toggle-title.active').siblings('.toggle-content').show();

      $('.toggle.toggle-enable .toggle-title').click(function() {
         $(this).closest('.toggle').find('.toggle-content').slideToggle(args);
         $(this).toggleClass('active');
      }); // toggle 

      $('.accordion .toggle-title').click(function () {
         if( !$(this).is('.active') ) {
            $(this).closest('.accordion').find('.toggle-title.active').toggleClass('active').next().slideToggle(args);
            $(this).toggleClass('active');
            $(this).next().slideToggle(args);
         } else {
            $(this).toggleClass('active');
            $(this).next().slideToggle(args);
         }     
      }); // accordion
   };

   var gmapSetup = function() {
      if ( $().gmap3 ) {
         $("#map").gmap3({
            map:{
               options:{
                  zoom: 17,
                  mapTypeId: 'orches_style',
                  mapTypeControlOptions: {
                     mapTypeIds: ['orches_style', google.maps.MapTypeId.SATELLITE, google.maps.MapTypeId.HYBRID]
                  },
                  scrollwheel: false
               }
            },
            getlatlng:{
               address:  "3 London Rd London SE1 6JZ United Kingdom",
               callback: function(results) {
                  if ( !results ) return;
                     $(this).gmap3('get').setCenter(new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng()));
                     $(this).gmap3({
                     marker:{
                     latLng:results[0].geometry.location
                     }
                  });
               }
            },
            styledmaptype:{
               id: "orches_style",
               options:{
                  name: "Orches Map"
               },
            },
         });
      }
   };

   var progressBar = function() {
      $('.progress-bar').on('on-appear', function() {
         $(this).each(function() {
            var percent = $(this).data('percent');

            $(this).find('.progress-animate').animate({
               "width": percent + '%'
            },3000);

            $(this).parent('.progress-single').find('.perc').addClass('show').animate({
               "width": percent + '%'
            },3000);
         });
      });
   }

   var ResponsiveMenu = {

      menuType: 'desktop',

      initial: function(winWidth) {
         ResponsiveMenu.menuWidthDetect(winWidth);
         ResponsiveMenu.menuBtnClick();
         ResponsiveMenu.parentMenuClick();
      },

      menuWidthDetect: function(winWidth) {
         var currMenuType = 'desktop';

         if (matchMedia('only screen and (max-width: 978px)').matches) {
            currMenuType = 'mobile';
         } // change menu type

         if (currMenuType !== ResponsiveMenu.menuType) {
            ResponsiveMenu.menuType = currMenuType;

            if (currMenuType === 'mobile') {
               var $mobileMenu = $('#mainnav').attr('id', 'mainnav-mobi').hide();
               $('#header').find('.header-wrap').after($mobileMenu);
               var hasChildMenu = $('#mainnav-mobi').find('li:has(ul)');
               hasChildMenu.children('ul').hide();
               hasChildMenu.children('a').after('<span class="btn-submenu"></span>');
               $('.btn-menu').removeClass('active');
             } else {
               var $desktopMenu = $('#mainnav-mobi').attr('id', 'mainnav').removeAttr('style');
               $desktopMenu.find('.sub-menu').removeAttr('style');
               $('#header').find('.span10').after($desktopMenu);
               $('.btn-submenu').remove();
             }
         } // clone and insert menu
      },

      menuBtnClick: function() {
         $('.btn-menu').on('click', function() {
            $('#mainnav-mobi').slideToggle(300);
            $(this).toggleClass('active');
         });
      }, // click on moblie button

      parentMenuClick: function() {
         $(document).on('click', '#mainnav-mobi li .btn-submenu', function(e) {
            if ($(this).has('ul')) {
               e.stopImmediatePropagation()
               $(this).next('ul').slideToggle(300);
               $(this).toggleClass('active');
            }
         });
      } // click on sub-menu button
   };

   var orAnimation = function() {
      $('.orches-animation').each( function() {
      var orElement = $(this),
         orAnimationClass = orElement.data('animation'),
         orAnimationDelay = orElement.data('animation-delay'),
         orAnimationOffset = orElement.data('animation-offset');

         orElement.css({
            '-webkit-animation-delay':  orAnimationDelay,
            '-moz-animation-delay':     orAnimationDelay,
            'animation-delay':          orAnimationDelay
         });
        
         orElement.waypoint(function() {
            orElement.addClass('animated').addClass(orAnimationClass);
            },{
               triggerOnce: true,
               offset: orAnimationOffset
         });
      });
   };

   var effectProject = function() {
      var effect = $('.project-container').data('portfolio-effect');
      $('.project-item').addClass('orches-animation');

      $('.project-container').waypoint(function(direction) {
         $('.project-item').each(function(idx, ele) {
            setTimeout(function() {
               $(ele).addClass('animated ' + effect);
            }, idx * 100);
         });
      }, {
         offset: '75%'
      });
   };

   var goTop = function() {
      $(window).scroll(function() {
         if ($(this).scrollTop() > 800 ) {
            $('.go-top').addClass('show');
         } else {
            $('.go-top').removeClass('show');
         }
      }); 
      
      $('.go-top').click(function() {
         $("html, body").animate({ scrollTop: 0 }, 1000 , 'easeInOutExpo');
         return false;
      });
   };

   var lastestTweets = function(){
      if ( $().tweet ) {
         $('.latest-tweets').each(function(){
            var $this = $(this);
            $this.tweet({
               username: $this.data('username'),
               join_text: "auto",
               avatar_size: null,
               count: $this.data('number'),
               template: "{text}",
               loading_text: "loading tweets...",
               modpath: $this.data('modpath'),        
            }); // tweet
         }); // lastest-tweets each
      }
   };

   var ajaxContactForm = function(){
      // http://www.bitrepository.com/a-simple-ajax-contact-form-with-php-validation.html
      $('.contact-form').each(function(){
         var $this = $(this); 
         $this.submit(function() {
            var str = $this.serialize();
            $.ajax({
               type: "POST",
               url:  $this.attr('action'),
               data: str,
               success: function(msg) {
                  // Message Sent? Show the 'Thank You' message and hide the form
                  var result;
                  if(msg == 'OK') {
                     result = '<div class="notification_ok">Your message has been sent. Thank you!</div>';
                  } else {
                     result = msg;
                  }
                  result = '<div class="result">' + result + '</div>';
                  $this.find('.note').html(result);
               }
            });
            return false;
         }); // submit

      }); // each contactform
   }; // contact

   var flickrFeed = function() {
      if ( $().jflickrfeed ) {
         $('.flickr-photos').each( function() {
            $(this).jflickrfeed({
               limit: 6,
               qstrings: {
                  id: '92231417@N05' // Your Flickr Id
               },
               itemTemplate: '<li><a href="{{link}}" title="{{title}}" target="_blank"><img src="{{image_s}}" alt="{{title}}" /></a></li>'
            });
         });
      }
   };

   var parallax = function() {
      $('.parallax-bg1').parallax("50%", 0.5);
      $('.parallax-bg2').parallax("50%", 0.3);
      $('.parallax-bg3').parallax("50%", 0.3);
      $('.parallax-bg4').parallax("50%", 0.3);
      $('.parallax-bg5').parallax("50%", 0.3);
      $('.parallax-bg6').parallax("50%", 0.3);
      $('.parallax-bg7').parallax("50%", 0.3);
      $('.parallax-bg8').parallax("50%", 0.4);
   };

   // Dom Ready
   $(function() {
      init_header();
      fullScreen();
      retinaLogos();
      responsiveTexts();
      projectIsotope();
      blogMasonry();
      testimonial();
      orSlider();
      toggles();
      gmapSetup();
      progressBar();
      orAnimation();
      effectProject();
      goTop()
      ajaxContactForm();
      flickrFeed();
      parallax();
      lastestTweets();
      
      // Initialize responsive menu
      ResponsiveMenu.initial($(window).width());
      $(window).resize(function() {
         ResponsiveMenu.menuWidthDetect($(this).width());
      });

      // Detect elements into viewport
      $('[data-waypoint-active="yes"]').waypoint(function() {
         $(this).trigger('on-appear');
      }, { offset: '90%' });

      $(window).on('load', function() {
         setTimeout(function() {
            $.waypoints('refresh');
         }, 100);
      });
   });

})(jQuery);