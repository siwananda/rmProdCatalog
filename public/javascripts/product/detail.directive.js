rmProduct
    .directive('enterPressed', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.enterPressed);
                });

                event.preventDefault();
            }
        });
    };
})
    .directive('priceCheck', function () {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {

                ctrl.$validators.priceCheck = function (modelValue, viewValue) {
                    return !!(!scope.product || viewValue > scope.product.pricing.cost);

                };
            }
        };
    })
    .directive('titleCheck', function () {
        return {
            require: 'ngModel',
            link: function (scope, elm, attrs, ctrl) {

                ctrl.$validators.titleCheck = function (modelValue, viewValue) {
                    console.log("through title check");
                    if (!scope.product) {
                        //product is not loaded yet, skip
                        return true;
                    }
                    if (viewValue !== "" && new RegExp("^[a-z]+$", "i").test(viewValue.charAt(0))) {
                        //Title must not be empty and has to start with alphabet
                        return true;
                    }
                    // it is invalid
                    return false;
                };
            }
        };
    });