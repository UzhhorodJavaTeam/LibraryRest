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

    var app = angular.module('app', ['ngRoute' ,'ui.bootstrap' ]);

// configure our routes
    app.config(function($routeProvider) {
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


        $routeProvider.otherwise({redirectTo: '/'});
    });

    app.directive('categorySidebar', function() {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/sidebar.html',
            controller: function ($scope, $http) {
                $scope.getAllCategories = function() {
                    $http.get('/categories')
                        .success(function(data) {
                            $scope.categories = data;
                        })
                        .error(function (data) {
                            toastr.error(data);
                        })
                };

                $scope.getAllCategories();
            }
        }
    });

    app.directive('newBookDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/new_book.html',
            controller: function ($scope, $http) {
                $scope.addBook = function (book){

                    $http.post('/categories/' + $scope.categoryId + '/books', book)
                        .success(function(){
                            $scope.booksCount++;
                            $scope.newbook = {};
                            $scope.uploadImage(book.name);
                            toastr.success("Successfully Added");

                        })
                        .error(function(){
                            toastr.error('Add failed')
                        })
                };
            }
        };
    });

    app.directive('editBookDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/delete_book.html',
            controller: function ($scope, $http) {
                $scope.getCurrentBook = function(bookId){
                    $http.get('/categories/' + $scope.categoryId + '/books/' + bookId).success(function(data){
                        $scope.currentBook = data;
                    });
                };

                $scope.editBook = function (book, bookId){
                    $http.put('/categories/' + $scope.categoryId + '/books/' + bookId, book)
                        .success(function(){
                            $scope.uploadImage(book.name);
                            toastr.success('Edited Successfully');
                        })
                        .error(function () {
                            toastr.error('Edit Failed');
                        })
                };
            }
        };
    });

    app.directive('deleteBookDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/edit_book.html',
            controller: function ($scope, $http) {
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
            }
        };
    });

    app.directive('deleteCategoryDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/delete_category.html',
            controller: function ($scope, $http) {
                $scope.prepareForDeleteCategory = function(categoryId){
                    $scope.categoryIdForDelete = categoryId;
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
            }
        };
    });

    app.directive('editCategoryDirective', function() {
        return { restrict: 'E',
            templateUrl: '/resources/pages/directives/edit_category.html',
            controller: function ($scope, $http) {
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
            }
        };
    });

    app.directive('addCategoryDirective', function () {
        return{
            restrict: 'E',
            templateUrl: '/resources/pages/directives/new_category.html',
            controller: function($scope, $http){
                $scope.newcategory = {};

                $scope.addCategory = function(category) {
                    $http.post('/categories', category)
                        .success(function (data) {
                            $scope.books = data; // response data
                            toastr.success('Added successfully');
                            $scope.newcategory = {};
                            $scope.getAllCategories();
                        })
                        .error(function (data) {
                            toastr.error(data);
                        })
                }
            }
        }
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

    app.controller('staticPagesController', function(){

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


    app.directive('bindFile', [function () {
        return {
            require: "ngModel",
            restrict: 'A',
            link: function ($scope, el, attrs, ngModel) {
                el.bind('change', function (event) {
                    ngModel.$setViewValue(event.target.files[0]);
                    $scope.$apply();
                });

                $scope.$watch(function () {
                    return ngModel.$viewValue;
                }, function (value) {
                    if (!value) {
                        el.val("");
                    }
                });
            }
        };
    }]);

    app.controller('BooksController',['filterFilter','$scope', '$routeParams','$http','$window', function(filterFilter ,$scope, $routeParams,  $http, $window) {
        $scope.categoryId = $routeParams.categoryId;
        $scope.bookId = $routeParams.bookId;
        $scope.newbook = {};
        $scope.maxSize = 12;
        $scope.currentPage = 1;
        $scope.searchText = '';

        $scope.theImage = null;
        $scope.thePdf = null;

        $scope.resetPdf = function () {
            $scope.thePdf = null;
        };
        $scope.resetImage = function () {
            $scope.theImage = null;
        };


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

        $scope.uploadImage = function (bookName) {
            var fd = new FormData();
            fd.append("bookName", bookName);
            fd.append("image", $scope.theImage);
            $http({
                method: 'POST',
                url: '/uploadImage',
                headers: {'Content-Type': undefined},
                data: fd,
                transformRequest: angular.identity
            }).success(function (data) {
                $scope.getBooksByCategoryAndPage($scope.currentPage);
                $scope.resetImage();
                fd = null;
            }).error(function (data) {
            });
            $scope.resetImage();
            $scope.uploadPdf(bookName);
        };

        $scope.uploadPdf = function (bookName) {
            var fd = new FormData();
            fd.append("bookName", bookName);
            fd.append("pdf", $scope.thePdf);
            $http({
                method: 'POST',
                url: '/uploadPdf',
                headers: {'Content-Type': undefined},
                data: fd,
                transformRequest: angular.identity
            }).success(function (data) {
                $scope.resetPdf();
                fd = null;
            }).error(function (data) {
            });
            $scope.resetPdf();
        };



    }]);


})();