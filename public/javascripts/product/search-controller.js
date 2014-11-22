var rmProduct = angular.module('rmProduct', ['ui.bootstrap', 'toaster']);
rmProduct.controller('productSearch', ['$scope', '$rootScope', '$http', function productSearch($scope, $rootScope, $http) {

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