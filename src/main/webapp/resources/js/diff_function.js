(function(){
    $('.selectpicker').selectpicker();
})();

$('.modal').on('hidden.bs.modal', function() {
    $(this).removeData('bs.modal');
});