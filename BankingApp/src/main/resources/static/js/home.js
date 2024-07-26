 document.addEventListener("DOMContentLoaded", function() {
        var sidebar = document.getElementById("sidebar");
        var sidebarToggle = document.getElementById("sidebar-toggle");
        var mainContent = document.getElementById("main-content");
        var header = document.getElementById("header");
        var footer = document.getElementById("footer");

        sidebarToggle.addEventListener("click", function() {
            if (sidebar.classList.contains("expanded-sidebar")) {
                sidebar.classList.remove("expanded-sidebar");
                sidebar.classList.add("collapsed-sidebar");
                sidebarToggle.innerHTML = '<i class="fas fa-arrow-right"></i>';
                sidebarToggle.style.left = "10px";
                mainContent.classList.remove("expanded-content");
                mainContent.classList.add("collapsed-content");
                header.classList.remove("expanded-header-footer");
                header.classList.add("collapsed-header-footer");
                footer.classList.remove("expanded-header-footer");
                footer.classList.add("collapsed-header-footer");
            } else {
                sidebar.classList.remove("collapsed-sidebar");
                sidebar.classList.add("expanded-sidebar");
                sidebarToggle.innerHTML = '<i class="fas fa-arrow-left"></i>';
                sidebarToggle.style.left = "260px";
                mainContent.classList.remove("collapsed-content");
                mainContent.classList.add("expanded-content");
                header.classList.remove("collapsed-header-footer");
                header.classList.add("expanded-header-footer");
                footer.classList.remove("collapsed-header-footer");
                footer.classList.add("expanded-header-footer");
            }
        });
    });