/**
 * Created by siwananda on 11/22/2014.
 */
angular.module('rb.product', ['ui.bootstrap', 'toaster'])
    .controller('productSearch', ['$scope', '$rootScope', '$http', function productSearch($scope, $rootScope, $http) {

        /**
         * Starts of with undefined selected product
         * @type {undefined}
         */
        $scope.selected = undefined;

        /**
         * Formats result on selection
         * @param model
         * @returns {*}
         */
        $scope.formatSearch = function (model) {
            if (model) {
                return parseInt(model)
            }
        };

        /**
         * Gets list of product with a given value
         * @param val value from search text box
         * @returns {*}
         */
        $scope.getProducts = function (val) {
            return $http.get('api/products/search', {
                params: {
                    idQuery: val
                }
            }).then(
                function (response) { //success callback
                    console.log("received data on querying " + val + ": ", response.data);
                    return response.data;
                },
                function () { //error callback
                    $scope.searchError = "Product with id: " + val + " can't be found!";
                    console.log("no product found with id: ", val);
                });
        };

        /**
         * Triggered on selection of item in the search box
         * @param $item the item selected
         * @param $model current model state
         * @param $label label passed
         */
        $scope.selectItem = function ($item, $model, $label) {
            $scope.searchError = null;//clears the error message
            console.log("Item selected: ", $label);
            loadProduct($item.id);
        };

        /**
         * Triggered through pressing of enter key (esc on selection suggestion and then press enter)
         */
        $scope.selectItemFE = function () {
            $scope.searchError = null;//clears the error message
            if (!isNaN(parseInt($scope.product.id))) {
                loadProduct(parseInt($scope.product.id));
            }
        };

        var loadProduct = function (itemId) {
            var url = 'api/products/' + itemId;
            $http.get(url).then(
                function (response) { //success callback
                    $rootScope.$emit("populateProduct", response.data);
                },
                function () { //error callback
                    $scope.searchError = "Product with id: " + itemId + " can't be found!";//shows error
                    $rootScope.$emit("populateProduct", null); //clears the form
                });
        };


    }])
    .controller('productUpdate', ['$scope', '$rootScope', '$http', 'toaster', function productUpdate($scope, $rootScope, $http, toaster) {

        /**
         * Sets up toaster
         */
        $scope.pop = function(){
            toaster.pop('success', "title", "text");
        };

        /**
         * Populates product form by assigning response to product instance
         * @param product passed from caller - can be null
         * @private
         */
        var _populateProductForm = function (product) {
            $scope.product = product;
        };

        /**
         * Listener to populateProduct event from search controller
         * @type {function()|*}
         */
        var prodSelectedListener = $rootScope.$on('populateProduct', function (event, product) {
            console.log("Received event", event);
            _populateProductForm(product);
        });

        /**
         * Saves the updated product. Show info whether successful or otherwise
         */
        $scope.saveProduct = function () {
            //Only triggers save on enter pressed if the form is valid
            if ($scope.productForm && $scope.productForm.$valid) {
                var url = 'api/products/' + $scope.product.id;
                $http.put(url, $scope.product)
                    .success(function (data) {
                        toaster.pop('success', "Product Saved", "The update for " + data.id + " is saved successfully");
                    }).
                    error(function (data) {
                        toaster.pop('error', "Product Save Failure", "Something bad happened, can you repeat that last step?");
                    });
            } else {
                console.log("attempted to save, but invalid: ", $scope.product);
            }
        };

        $scope.$on('$destroy', prodSelectedListener);
    }])
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


