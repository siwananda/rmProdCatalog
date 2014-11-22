rmProduct.controller('productUpdate', ['$scope', '$rootScope', '$http', 'toaster', function productUpdate($scope, $rootScope, $http, toaster) {

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