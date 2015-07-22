(function () {
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

    var app = angular.module('app', ['ngRoute', 'ui.bootstrap', 'nya.bootstrap.select']);

// configure our routes
    app.config(["$routeProvider", function ($routeProvider) {
        $routeProvider
            .when('/main/categories/:categoryId/books', {
                templateUrl: '/resources/pages/books/books_list.html',
                controller: 'BooksController'
            })

            .when('/main/categories/:categoryId/books/:bookId', {
                templateUrl: '/resources/pages/books/book_details.html',
                controller: 'bookDetailsController'
            })

            .when('/about', {
                templateUrl: '/resources/pages/static pages/about.html',
                controller: 'staticPagesController'
            })

            .when('/contact', {
                templateUrl: '/resources/pages/static pages/contact.html',
                controller: 'staticPagesController'
            })

            .when('/register', {
                templateUrl: '/resources/pages/static pages/register.html',
                controller: 'UserController'
            })

            .when('/users', {
                templateUrl: '/resources/pages/static pages/users.html',
                controller: "UserController"
            })

            .when('/profile', {
                templateUrl: '/resources/pages/static pages/profile.html',
                controller: 'UserController'
            })

            .when('/login', {
                templateUrl: '/resources/pages/static pages/login.html',
                controller: 'UserController'
            });


        $routeProvider.otherwise({redirectTo: '/'});
    }]);

    app.directive('categorySidebar', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/sidebar.html',
            controller: function ($scope, $http, myService) {
                myService.async().then(function () {
                    $scope.userDetails = myService.data();
                    $scope.authorized = myService.authorized();
                    $scope.role = myService.role();
                    $scope.adminAction = false;
                    if ($scope.role === "ROLE_ADMIN") {
                        $scope.adminAction = true;
                    } else {
                        $scope.adminAction = false;
                    }

                    $scope.accessCreateBook = false;
                    if ($scope.role === "ROLE_USER" || $scope.role === "ROLE_ADMIN") {
                        $scope.accessCreateBook = true;
                    } else {
                        $scope.accessCreateBook = false;
                    }
                });

                $scope.getAllCategories = function () {
                    $http.get('/categories')
                        .success(function (data) {
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

    app.directive('newBookDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/new_book.html',
            controller: function ($scope, $http) {
                $scope.addBook = function (book) {

                    $http.post('/categories/' + $scope.categoryId + '/books', book)
                        .success(function (data) {
                            $scope.booksCount++;
                            $scope.newbook = {};
                            $scope.uploadImage(book.name);
                            toastr.success("Successfully Added");

                        })
                        .error(function () {
                            toastr.error('Add failed')
                        })
                };
            }
        };
    });

    app.directive('editBookDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/delete_book.html',
            controller: function ($scope, $http) {
                $scope.getCurrentBook = function (bookId) {
                    $http.get('/categories/' + $scope.categoryId + '/books/' + bookId).success(function (data) {
                        $scope.currentBook = data;
                    });
                };

                $scope.editBook = function (book, bookId) {
                    $http.put('/categories/' + $scope.categoryId + '/books/' + bookId, book)
                        .success(function () {
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

    app.directive('deleteBookDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/edit_book.html',
            controller: function ($scope, $http) {
                $scope.prepareForDelete = function (bookId) {
                    $scope.bookIdForDelete = bookId;
                };

                $scope.deleteBook = function (bookId) {
                    $http.delete('/categories/' + $scope.categoryId + "/books/" + bookId)
                        .success(function (data) {
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

    app.directive('deleteCategoryDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/delete_category.html',
            controller: function ($scope, $http) {
                $scope.prepareForDeleteCategory = function (categoryId) {
                    $scope.categoryIdForDelete = categoryId;
                };


                $scope.deleteCategory = function (categoryId) {
                    $http.delete('/categories/' + categoryId)
                        .success(function (data) {
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

    app.directive('editCategoryDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/edit_category.html',
            controller: function ($scope, $http) {
                $scope.getCurrentCategory = function (categoryId) {
                    $http.get('/categories/' + categoryId).success(function (data) {
                        $scope.currentCategory = data;
                    });
                };


                $scope.editCategory = function (category, categoryId) {
                    $http.put('/categories/' + categoryId, category)
                        .success(function () {
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
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/new_category.html',
            controller: function ($scope, $http) {
                $scope.newcategory = {};

                $scope.addCategory = function (category) {
                    $http.post('/categories', category)
                        .success(function (data) {
                            $scope.books = data; // response data
                            toastr.success('Added successfully');
                            $scope.newcategory = {};
                            $scope.getAllCategories();
                        })
                        .error(function (data) {
                            toastr.error('Add Failed');
                        })
                }
            }
        }
    });

    app.directive('addAuthorDirective', function () {
        return {
            restrict: 'E',
            templateUrl: '/resources/pages/directives/new_author.html',
            controller: function ($http, $scope, $routeParams) {
                $scope.categoryId = $routeParams.categoryId;
                $scope.bookId = $routeParams.bookId;
                $scope.author = {};

                $scope.addAuthor = function (author) {
                    $http.post('/authors', author).success(function () {
                        toastr.success('Added Successfully');
                        $scope.getAllAuthors();
                        $scope.author = '';
                    }).error(function () {
                        toastr.error('Add failed!')
                    });
                };

                $scope.prepareForDeleteAuthor = function (authorId) {
                    $scope.authorIdForDelete = bookId;
                };

                $scope.resetAuthorField = function () {
                    $scope.author = '';
                }
            }
        }
    });


    app.directive('paginationDirective', function () {
        return {
            restrict: 'E',
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

    app.controller('staticPagesController', function () {

    });

    app.controller('bookDetailsController', ['$scope', '$routeParams', '$http', function ($scope, $routeParams, $http) {
        var categoryId = $routeParams.categoryId;
        $scope.bookId = $routeParams.bookId;

        $http.get('/categories/' + categoryId + '/books/' + $scope.bookId).success(function (data) {
            $scope.book = data;
        });
    }]);
    app.factory('myService', function ($http, $q) {
        var deffered = $q.defer();
        var data = [];
        var role = "";
        var authorized = false;
        var myService = {};

        myService.async = function () {
            $http.get('/users/user')
                .success(function (user) {
                    data = user;
                    authorized = true;
                    deffered.resolve();
                });
            return deffered.promise;
        };
        myService.role = function () {
            role = data.authorities[0].authority;
            return role;
        };

        myService.authorized = function () {
            return authorized;
        };

        myService.data = function () {
            return data;
        };

        return myService;
    });


    app.controller('UserController', ['$scope', '$http', '$location', function ($scope, $http, $location) {
        $scope.newuser = {};

        $scope.register = function (user) {
            $http.post('/register', user)

                .success(function () {
                    toastr.success('Successfully registered');
                    $location.url('/');
                })
                .error(function () {
                    toastr.error("Register Failed");
                })
        };

        $scope.getAllUsers = function () {
            $http.get('/users').success(function (userList) {
                $scope.users = userList;
            }).error(function (data) {
                console.log(data);
            })
        };

        $scope.getAllUsers();
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

    app.directive("starRating", function () {
        return {
            restrict: "EA",
            template: "<ul class='rating' ng-class='{readonly: readonly}'>" +
            "  <li ng-repeat='star in stars' ng-class='star' ng-click='toggle($index)'>" +
            "    <i class='fa fa-star'></i>" + //&#9733
            "  </li>" +
            "</ul>",
            scope: {
                ratingValue: "=ngModel",
                max: "=?", //optional: default is 5
                onRatingSelected: "&?",
                readonly: "=?"
            },
            link: function (scope, elem, attrs) {
                if (scope.max == undefined) {
                    scope.max = 5;
                }
                function updateStars() {
                    scope.stars = [];
                    for (var i = 0; i < scope.max; i++) {
                        scope.stars.push({
                            filled: i < scope.ratingValue
                        });
                    }
                };
                scope.toggle = function (index) {
                    if (scope.readonly == undefined || scope.readonly == false) {
                        scope.ratingValue = index + 1;
                        scope.onRatingSelected({
                            rating: index + 1
                        });
                    }
                };
                scope.$watch("ratingValue", function (oldVal, newVal) {
                    if (newVal) {
                        updateStars();
                    }
                });
            }
        };
    });

    app.controller('BooksController', ['filterFilter', '$scope', '$routeParams', '$http', '$window', '$filter', function (filterFilter, $scope, $routeParams, $http, $window, $filter) {
        $scope.categoryId = $routeParams.categoryId;
        $scope.bookId = $routeParams.bookId;
        $scope.newbook = {};
        $scope.maxSize = 12;
        $scope.currentPage = 1;
        $scope.searchText = '';
        $scope.rating = 1;
        $scope.isReadonly = true;
        $scope.theImage = null;
        $scope.thePdf = null;

        $scope.refreshData = function (value, bookId, id) {
            var fd = new FormData();
            fd.append("value", value);
            fd.append("bookId", bookId);
            fd.append("userId", id);
            $http({
                method: 'POST',
                url: '/votes',
                headers: {'Content-Type': undefined},
                data: fd,
                transformRequest: angular.identity
            })
                .success(function (data) {
                    $scope.getBooksByCategoryAndPage($scope.currentPage);
                    toastr.success("Vote recorded")
                })
                .error(function (data) {
                    toastr.error(data)
                });
        };

        $scope.resetPdf = function () {
            $scope.thePdf = null;
        };
        $scope.resetImage = function () {
            $scope.theImage = null;
        };

        $scope.getAllBooks = function () {
            $http.get('/categories/' + $scope.categoryId + "/books").success(function (data) {
                $scope.allBooks = data; // response data
                $scope.getBooksBySearch($scope.allBooks, $scope.searchText);
            });
        };

        $scope.getAllAuthors = function () {
            $http.get('/authors').success(function (data) {
                $scope.authors = data;
            })
        };

        $scope.getAllAuthors();

        $scope.getCountBooks = function () {
            $http.get('/categories/' + $scope.categoryId + "/books").success(function (data) {
                $scope.booksCount = data.length;
            });
        };

        $scope.getCountBooks();

        $scope.getBooksBySearch = function (data, text) {
            if (text != '' && text.name != '') {
                $scope.searchedData = filterFilter(data, text);
                if ($scope.searchedData == '') {
                    toastr.info('The required book not found');
                }
            } else {
                $scope.searchedData = '';
                $scope.getBooksByCategoryAndPage($scope.currentPage);
            }
        };

        $scope.getBooksByCategoryAndPage = function (page) {
            $http.get('/categories/' + $scope.categoryId + '/books/page=' + page).success(function (data) {
                $scope.books = data; // response data
            });
        };

        $scope.getBooksByPage = function (page) {
            $http.get('/books/page=' + page).success(function (data) {
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

    $(document)
        .on('show.bs.modal', '.modal', function (event) {
            $(this).appendTo($('body'));
        })
        .on('shown.bs.modal', '.modal.in', function (event) {
            setModalsAndBackdropsOrder();
        })
        .on('hidden.bs.modal', '.modal', function (event) {
            setModalsAndBackdropsOrder();
        });

    function setModalsAndBackdropsOrder() {
        var modalZIndex = 1040;
        $('.modal.in').each(function (index) {
            var $modal = $(this);
            modalZIndex++;
            $modal.css('zIndex', modalZIndex);
            $modal.next('.modal-backdrop.in').addClass('hidden').css('zIndex', modalZIndex - 1);
        });
        $('.modal.in:visible:last').focus().next('.modal-backdrop.in').removeClass('hidden');
    }


})
()
