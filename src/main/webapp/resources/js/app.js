(function(){
'use strict';

toastr.options = {
    "closeButton": true,
    "debug": false,
    "newestOnTop": false,
    "progressBar": true,
    "positionClass": "toast-top-right",
    "preventDuplicates": false,
    "onclick": null,
    "showDuration": "300",
    "hideDuration": "1000",
    "timeOut": "5000",
    "extendedTimeOut": "1000",
    "showEasing": "swing",
    "hideEasing": "linear",
    "showMethod": "fadeIn",
    "hideMethod": "fadeOut"
};

var app = angular.module('app', ['ngRoute' ,'ui.bootstrap']);

// configure our routes
app.config(function($routeProvider, $locationProvider) {
    $routeProvider

        .when('/main/categories/:categoryId/books', {
            templateUrl : '/resources/pages/books/books_list.html',
            controller  : 'BooksController'
        })

        .when('/main/categories/:categoryId/books/:bookId', {
            templateUrl : '/resources/pages/books/book_details.html',
            controller  : 'bookDetailsController'
        })

        .when('/about', {
            templateUrl : '/resources/pages/static pages/about.html',
            controller  : 'staticPagesController'
        })

        .when('/contact', {
            templateUrl : '/resources/pages/static pages/contact.html',
            controller  : 'staticPagesController'
        })

        .when('/register', {
            templateUrl : '/resources/pages/static pages/register.html',
            controller  : 'UserController'
        })

        .when('/login', {
            templateUrl : '/resources/pages/static pages/login.html',
            controller  : 'UserController'
        });

    $locationProvider.html5Mode(true);

    $routeProvider.otherwise({redirectTo: '/'});
});

app.directive('categorySidebar', function() {
    return {
        restrict: 'E',
        templateUrl: '/resources/pages/directives/sidebar.html',
        controller: 'categoryController'
    }
});

app.directive('newBookDirective', function() {
    return { restrict: 'E',
        templateUrl: '/resources/pages/directives/new_book.html',
        controller: 'BooksController'
    };
});

app.directive('editBookDirective', function() {
    return { restrict: 'E',
        templateUrl: '/resources/pages/directives/delete_book.html',
        controller: 'BooksController'
    };
});

app.directive('deleteBookDirective', function() {
    return { restrict: 'E',
        templateUrl: '/resources/pages/directives/edit_book.html',
        controller: 'BooksController'
    };
});

    app.directive('deleteCategoryDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/delete_category.html',
            controller: 'categoryController'
        };
    });

    app.directive('editCategoryDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/edit_category.html',
            controller: 'categoryController'
        };
    });

app.directive('paginationDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/pagin.html',
            controller: function ($scope) {

                $scope.setPage = function (pageNo) {
                    $scope.currentPage = pageNo;
                };

                $scope.maxSize = 10;
                $scope.currentPage = 2;
            }
        };
    });

app.directive('addCategoryDirective', function () {
       return{
           restrict: 'E',
           templateUrl: '/resources/pages/directives/new_category.html',
           controller: 'categoryController'
       }
    });

app.controller('staticPagesController', function(){

});

app.controller('categoryController', function($scope, $http){
        $scope.newcategory = {};

        $scope.getAllCategories = function() {
            $http.get('/categories')
                .success(function(data) {
                    $scope.categories = data
            })
                .error(function (data) {
                    toastr.error(data);
                })
        };

        $scope.getAllCategories();

        $scope.addCategory = function(category){
            $http.post('/categories', category)
                .success(function(data) {
                $scope.books = data; // response data
                toastr.success('Added successfully');
                $scope.newcategory = {};
                $scope.getAllCategories();
            })
                .error(function(data){
                toastr.error(data);
            });
        };

        $scope.prepareForDeleteCategory = function(categoryId){
            $scope.categoryIdForDelete = categoryId;
        };

        $scope.getCurrentCategory = function(categoryId){
            $http.get('/categories/'+ categoryId).success(function(data){
                $scope.currentCategory = data;
            });
        };


        $scope.editCategory = function (category, categoryId){
            $http.put('/categories/' + categoryId, category)
                .success(function(){
                    $scope.getAllCategories();
                    toastr.success('Edited Successfully');
                })
                .error(function () {
                    toastr.error('Edit Failed');
                })
        };

        $scope.deleteCategory = function(categoryId) {
            $http.delete('/categories/' + categoryId)
                .success(function(data) {
                    $scope.getAllCategories();
                    toastr.success(data);
                })
                .error(function () {
                    toastr.error('Delete Failed');
                });
        };

    });

app.controller('bookDetailsController',['$scope','$routeParams','$http', function($scope, $routeParams,  $http) {
    var categoryId = $routeParams.categoryId;
    $scope.bookId = $routeParams.bookId;

      $http.get('/categories/' + categoryId + '/books/' + $scope.bookId).success(function(data){
            $scope.book = data;
        });
}]);

    app.controller('UserController',['$scope','$http', function($scope, $http) {
    $scope.newuser = {};

        $scope.register = function (user) {
          $http.post('/register', user)

              .success(function (data) {
                toastr.success(data);
              })
              .error(function () {
                  toastr.error("Register Failed");
              })
        };
}]);


app.controller('BooksController',['filterFilter','$scope', '$routeParams','$http', function(filterFilter ,$scope, $routeParams,  $http) {
    $scope.categoryId = $routeParams.categoryId;
    $scope.bookId = $routeParams.bookId;
    $scope.newbook = {};
    $scope.maxSize = 12;
    $scope.currentPage = 1;
    $scope.searchText = '';


    $scope.getAllBooks = function(){
        $http.get('/categories/' + $scope.categoryId + "/books").success(function(data)
        {
            $scope.allBooks = data; // response data
            $scope.getBooksBySearch($scope.allBooks, $scope.searchText);
        });
    };

    $scope.getAllAuthors = function(){
        $http.get('/authors').success(function (data) {
            $scope.authors = data;
        })
    };

    $scope.getAllAuthors();

    $scope.getCountBooks = function(){
        $http.get('/categories/' + $scope.categoryId + "/books").success(function(data)
        {
            $scope.booksCount = data.length;
        });
    };

    $scope.getCountBooks();

    $scope.getBooksBySearch = function(data, text){
        if(text != '' && text.name != ''){
            $scope.searchedData = filterFilter(data, text);
            if($scope.searchedData == ''){
                toastr.info('The required book not found');
            }
        }else{
            $scope.searchedData = '';
            $scope.getBooksByCategoryAndPage($scope.currentPage);
        }
    };

    $scope.getBooksByCategoryAndPage = function(page){
        $http.get('/categories/' + $scope.categoryId + '/books/page='+page).success(function(data)
        {
            $scope.books = data; // response data
        });
    };

    $scope.getBooksByPage = function(page){
        $http.get('/books/page='+page).success(function(data)
        {
            $scope.booksByPage = data; // response data
        });
    };

    $scope.getBooksByCategoryAndPage($scope.currentPage);

    $scope.addBook = function (book){
        $http.post('/categories/' + $scope.categoryId + '/books', book)
            .success(function(){
                $scope.booksCount++;
                $scope.getBooksByCategoryAndPage($scope.currentPage);
                $scope.newbook = {};
                toastr.success('Added Successfully');
        })
            .error(function(){
                toastr.error('Add failed')
        })
    };

    $scope.prepareForDelete = function(bookId){
        $scope.bookIdForDelete = bookId;
    };

    $scope.deleteBook = function(bookId) {
        $http.delete('/categories/' + $scope.categoryId + "/books/" + bookId)
            .success(function(data) {
                $scope.booksCount--;
                $scope.getBooksByCategoryAndPage($scope.currentPage);
                toastr.success(data);
            })
            .error(function () {
                toastr.error('Delete Failed');
            });
    };

    $scope.getCurrentBook = function(bookId){
        $http.get('/categories/' + $scope.categoryId + '/books/' + bookId).success(function(data){
            $scope.currentBook = data;
        });
    };

    $scope.editBook = function (book, bookId){
        $http.put('/categories/' + $scope.categoryId + '/books/' + bookId, book)
            .success(function(){
                $scope.getBooksByCategoryAndPage($scope.currentPage);
                toastr.success('Edited Successfully');
            })
            .error(function () {
                toastr.error('Edit Failed');
            })
    };

}]);
})();