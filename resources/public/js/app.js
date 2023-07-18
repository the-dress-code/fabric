document.addEventListener('DOMContentLoaded', function() {
  document.body.addEventListener("click", function(e) {
    var el = e.target;
    var confirmMessage = el.getAttribute("data-confirm");

    if(!!confirmMessage) {
      e.preventDefault();

      if(confirmMessage && confirm(confirmMessage)) {
        el.closest('form').submit();
      }
    }
  });
});
