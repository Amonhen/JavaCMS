var firstRow = document.getElementById("firstRow");
var menu = document.getElementById("menu");
$("#menu").click(function (event) {
    console.log(event.target.innerHTML);
});
var menuList = document.getElementById("menu-ul");
$("menu").css("width");


function openDiv() {
    $("#info-div").animate(
        {
            width:"1000px",
            height:"500px"
        },
        {
            duration:1000
        }
    );
}

function closeDiv() {

    $("#info-div").animate(
        {
            width:"10px",
            height:"10px"
        },
        {
            duration:1000,
            complete: function () {
                $("#info-div").html(
                    "<div class='menu-object'>" +
                    "<img src='profile.jpg'>" +
                    "<p>Nullam rhoncus in nisi et auctor.Donec dolor tellus, " +
                    "condimentum non sapien a, iaculis fringilla quam.Etiam non " +
                    "viverra risus, non sagittis dolor.Vestibulum ultricies velit ut aliquet scelerisque. " +
                    "</p> </div>");
            }
        }
    );
    openDiv();
}


menuList.addEventListener("click",function (event) {
    if (event.target.localName === "li") {
        switch (event.target.innerHTML) {
            case "Profile" : {
                //closeDiv();
                break;
            }
            case "Articles" : {
               // closeDiv();
                break;
            }
            case "Chat" : {
               // closeDiv();
                break;
            }
            case "Setting" : {
              //  closeDiv();
                break;
            }
            case "Users" : {
              //  closeDiv();
                break;
            }
            case "DataBase" : {
              //  closeDiv();
                break;
            }
            case "Logout" : {
               // closeDiv();
                break;
            }
            default : {
                console.log("Menu-switch-menu.");
            }
        }
    }
});
