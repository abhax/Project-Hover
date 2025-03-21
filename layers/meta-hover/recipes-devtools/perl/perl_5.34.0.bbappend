# Fix for perl build issues
EXTRA_OEMAKE += "-j1"

# Disable parallel make for nativesdk-perl
do_compile:prepend:class-nativesdk() {
    export PERL_MAKEMAKER_ARGS="INSTALLDIRS=perl"
}

# Skip failing tests
do_configure:append:class-nativesdk() {
    # Fix possible issue with stale Makefile
    cd ${B}
    rm -f cpan/*/Makefile cpan/*/pm_to_blib
}

# Additional environment setup to fix build issues
EXTRA_OECONF += "--disable-debug" 