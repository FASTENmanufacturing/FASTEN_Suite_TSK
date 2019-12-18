if(isMobile()) {
    $('a.ui-link').click(function() {
        $(this).prop('disabled', true);
        showBar();
    });
}