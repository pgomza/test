webpackJsonp([8],{

/***/ 670:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _reactRedux = __webpack_require__(368);

	var _reactRouter = __webpack_require__(211);

	var _actions = __webpack_require__(555);

	var _guestActions = __webpack_require__(556);

	var _MainPanel = __webpack_require__(671);

	var _MainPanel2 = _interopRequireDefault(_MainPanel);

	var _MainHeader = __webpack_require__(673);

	var _MainHeader2 = _interopRequireDefault(_MainHeader);

	var _loginService = __webpack_require__(384);

	var loginService = _interopRequireWildcard(_loginService);

	var _config = __webpack_require__(388);

	var _config2 = _interopRequireDefault(_config);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var RootContainer = function (_Component) {
	  (0, _inherits3.default)(RootContainer, _Component);

	  function RootContainer() {
	    var _ref;

	    var _temp, _this, _ret;

	    (0, _classCallCheck3.default)(this, RootContainer);

	    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
	      args[_key] = arguments[_key];
	    }

	    return _ret = (_temp = (_this = (0, _possibleConstructorReturn3.default)(this, (_ref = RootContainer.__proto__ || (0, _getPrototypeOf2.default)(RootContainer)).call.apply(_ref, [this].concat(args))), _this), _this.componentDidMount = function () {
	      setInterval(function () {
	        return _this.props.dispatch((0, _actions.fetchStays)());
	      }, _config2.default.refreshInterval);
	      setInterval(function () {
	        return _this.props.dispatch((0, _guestActions.fetchGuests)());
	      }, _config2.default.refreshInterval);

	      if (_this.props.login.status === loginService.LOGGED_OUT) {
	        _this.deleteTokenAndChangeLocation();
	      } else if (_this.props.login.status === loginService.LOGGED_IN && _this.props.location.pathname === '/login') {
	        _this.redirectIfAlreadyLoggedIn();
	      } else {
	        _this.props.dispatch((0, _actions.fetchStays)());
	        _this.props.dispatch((0, _guestActions.fetchGuests)());
	      }
	    }, _this.componentWillReceiveProps = function (nextProps) {
	      if (nextProps.login.status === loginService.TOKEN_EXPIRED) {
	        nextProps.dispatch({ type: loginService.LOGGED_OUT });
	        _this.deleteTokenAndChangeLocation();
	      } else if (nextProps.login.status === loginService.LOGGED_OUT && nextProps.location.pathname !== '/login') {
	        _this.deleteTokenAndChangeLocation();
	      } else if (nextProps.login.status === loginService.LOGGED_IN && nextProps.location.pathname === '/login') {
	        _this.redirectIfAlreadyLoggedIn();
	      }
	    }, _this.redirectIfAlreadyLoggedIn = function () {
	      _reactRouter.browserHistory.push('/');
	    }, _this.deleteTokenAndChangeLocation = function () {
	      loginService.deleteToken();
	      _reactRouter.browserHistory.push('/login');
	    }, _this.handleLogout = function () {
	      _this.props.dispatch({ type: loginService.LOGGED_OUT });
	    }, _temp), (0, _possibleConstructorReturn3.default)(_this, _ret);
	  }

	  (0, _createClass3.default)(RootContainer, [{
	    key: 'render',
	    value: function render() {
	      var login = this.props.login;

	      var isLoggedIn = login.status === loginService.LOGGED_IN;

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(_MainHeader2.default, { isLoggedIn: isLoggedIn, handleLogout: this.handleLogout }),
	        _react2.default.createElement(
	          'div',
	          { className: 'container' },
	          _react2.default.createElement(
	            'div',
	            { className: 'row' },
	            _react2.default.createElement(
	              'div',
	              { className: 'col-xs-12' },
	              _react2.default.createElement(
	                _MainPanel2.default,
	                null,
	                this.props.children
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return RootContainer;
	}(_react.Component);

	RootContainer.propTypes = {
	  children: _react.PropTypes.object.isRequired
	};

	var mapStateToProps = function mapStateToProps(state) {
	  return {
	    login: state.login,
	    location: state.location
	  };
	};

	RootContainer = (0, _reactRedux.connect)(mapStateToProps, null)(RootContainer);
	exports.default = RootContainer;

/***/ },

/***/ 671:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	__webpack_require__(672);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var MainPanel = function (_Component) {
	  (0, _inherits3.default)(MainPanel, _Component);

	  function MainPanel() {
	    (0, _classCallCheck3.default)(this, MainPanel);
	    return (0, _possibleConstructorReturn3.default)(this, (MainPanel.__proto__ || (0, _getPrototypeOf2.default)(MainPanel)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(MainPanel, [{
	    key: 'render',
	    value: function render() {
	      return _react2.default.createElement(
	        'div',
	        { className: 'main-panel' },
	        this.props.children
	      );
	    }
	  }]);
	  return MainPanel;
	}(_react.Component);

	MainPanel.propTypes = {
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = MainPanel;

/***/ },

/***/ 672:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 673:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(316);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(321);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(322);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(326);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(360);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _reactRouter = __webpack_require__(211);

	__webpack_require__(674);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var MainHeader = function (_Component) {
	  (0, _inherits3.default)(MainHeader, _Component);

	  function MainHeader() {
	    (0, _classCallCheck3.default)(this, MainHeader);
	    return (0, _possibleConstructorReturn3.default)(this, (MainHeader.__proto__ || (0, _getPrototypeOf2.default)(MainHeader)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(MainHeader, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          isLoggedIn = _props.isLoggedIn,
	          handleLogout = _props.handleLogout;


	      return _react2.default.createElement(
	        'nav',
	        { className: 'navbar navbar-default main-header' },
	        _react2.default.createElement(
	          'div',
	          { className: 'container' },
	          isLoggedIn && _react2.default.createElement(
	            'div',
	            null,
	            _react2.default.createElement(
	              'ul',
	              { className: 'nav navbar-nav' },
	              _react2.default.createElement(
	                'li',
	                null,
	                _react2.default.createElement(
	                  _reactRouter.Link,
	                  { to: '/' },
	                  'Hotel stays'
	                )
	              ),
	              _react2.default.createElement(
	                'li',
	                null,
	                _react2.default.createElement(
	                  _reactRouter.Link,
	                  { to: '/guests' },
	                  'Guests'
	                )
	              ),
	              _react2.default.createElement(
	                'li',
	                { className: 'dropdown' },
	                _react2.default.createElement(
	                  'a',
	                  { href: '#', className: 'dropdown-toggle', 'data-toggle': 'dropdown' },
	                  'Services ',
	                  _react2.default.createElement('span', { className: 'caret' })
	                ),
	                _react2.default.createElement(
	                  'ul',
	                  { className: 'dropdown-menu' },
	                  _react2.default.createElement(
	                    'li',
	                    null,
	                    _react2.default.createElement(
	                      _reactRouter.Link,
	                      { to: '/breakfast' },
	                      'Breakfast'
	                    )
	                  ),
	                  _react2.default.createElement(
	                    'li',
	                    null,
	                    _react2.default.createElement(
	                      _reactRouter.Link,
	                      { to: '/carpark' },
	                      'Car park'
	                    )
	                  ),
	                  _react2.default.createElement(
	                    'li',
	                    null,
	                    _react2.default.createElement(
	                      _reactRouter.Link,
	                      { to: '/petcare' },
	                      'Pet care'
	                    )
	                  ),
	                  _react2.default.createElement(
	                    'li',
	                    null,
	                    _react2.default.createElement(
	                      _reactRouter.Link,
	                      { to: '/taxi' },
	                      'Taxi'
	                    )
	                  )
	                )
	              )
	            ),
	            _react2.default.createElement(
	              'ul',
	              { className: 'nav navbar-nav navbar-right' },
	              _react2.default.createElement(
	                'li',
	                null,
	                _react2.default.createElement(
	                  _reactRouter.Link,
	                  { to: '#',
	                    onClick: function onClick(e) {
	                      e.preventDefault();handleLogout();
	                    } },
	                  _react2.default.createElement('i', { className: 'fa fa-sign-out fa-lg', 'aria-hidden': 'true' }),
	                  ' Log out'
	                )
	              )
	            )
	          ),
	          !isLoggedIn && _react2.default.createElement(
	            'ul',
	            { className: 'nav navbar-nav navbar-right' },
	            _react2.default.createElement(
	              'li',
	              null,
	              _react2.default.createElement(
	                _reactRouter.Link,
	                { to: '/login' },
	                _react2.default.createElement('i', { className: 'fa fa-sign-in fa-lg', 'aria-hidden': 'true' }),
	                ' Log in'
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return MainHeader;
	}(_react.Component);

	MainHeader.propTypes = {
	  isLoggedIn: _react.PropTypes.bool.isRequired,
	  handleLogout: _react.PropTypes.func.isRequired
	};

	exports.default = MainHeader;

/***/ },

/***/ 674:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 675:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _loginService = __webpack_require__(384);

	var loginService = _interopRequireWildcard(_loginService);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var loginReducer = function loginReducer() {
	  var state = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { status: loginService.getInitialState() };
	  var action = arguments[1];

	  switch (action.type) {
	    case loginService.LOGIN_NEW:
	    case loginService.LOGIN_PENDING:
	    case loginService.LOGIN_WRONG_CREDENTIALS:
	    case loginService.LOGIN_ERROR:
	    case loginService.TOKEN_EXPIRED:
	    case loginService.LOGGED_IN:
	    case loginService.LOGGED_OUT:
	      return (0, _extends3.default)({}, state, { status: action.type });

	    default:
	      return state;
	  }
	};

	exports.default = loginReducer;

/***/ },

/***/ 676:
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _actions = __webpack_require__(555);

	var types = _interopRequireWildcard(_actions);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var genericReducer = function genericReducer() {
	  var state = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { status: "INITIAL" };
	  var action = arguments[1];

	  switch (action.type) {
	    case types.UPDATE_GENERIC_NEW:
	    case types.UPDATE_GENERIC_PENDING:
	    case types.UPDATE_GENERIC_COMPLETED:
	    case types.UPDATE_GENERIC_ERROR:

	    case types.POST_GENERIC_NEW:
	    case types.POST_GENERIC_PENDING:
	    case types.POST_GENERIC_COMPLETED:
	    case types.POST_GENERIC_ERROR:

	    case types.DELETE_GENERIC_NEW:
	    case types.DELETE_GENERIC_PENDING:
	    case types.DELETE_GENERIC_COMPLETED:
	    case types.DELETE_GENERIC_ERROR:
	      return (0, _extends3.default)({}, state, { status: action.type });

	    default:
	      return state;
	  }
	};

	exports.default = genericReducer;

/***/ },

/***/ 677:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _typeof2 = __webpack_require__(327);

	var _typeof3 = _interopRequireDefault(_typeof2);

	var _assign = __webpack_require__(276);

	var _assign2 = _interopRequireDefault(_assign);

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _actions = __webpack_require__(555);

	var types = _interopRequireWildcard(_actions);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var staysReducer = function staysReducer() {
	  var state = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { entries: {}, status: types.FETCH_STAYS_NEW };
	  var action = arguments[1];

	  var _ret = function () {
	    switch (action.type) {
	      case types.FETCH_STAYS_NEW:
	      case types.FETCH_STAYS_PENDING:
	      case types.FETCH_STAYS_ERROR:
	        return {
	          v: (0, _extends3.default)({}, state, { status: action.type })
	        };
	      case types.FETCH_STAYS_COMPLETED:
	        var entries = {};
	        action.stays.forEach(function (stay) {
	          var temp = {};
	          temp[stay.pin] = stay;
	          (0, _assign2.default)(entries, temp);
	        });
	        return {
	          v: { entries: entries, status: action.type }
	        };

	      default:
	        return {
	          v: state
	        };
	    }
	  }();

	  if ((typeof _ret === 'undefined' ? 'undefined' : (0, _typeof3.default)(_ret)) === "object") return _ret.v;
	};

	exports.default = staysReducer;

/***/ },

/***/ 678:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _assign = __webpack_require__(276);

	var _assign2 = _interopRequireDefault(_assign);

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _guestActions = __webpack_require__(556);

	var types = _interopRequireWildcard(_guestActions);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var guestsReducer = function guestsReducer() {
	  var state = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { items: [], status: types.FETCH_GUESTS_NEW };
	  var action = arguments[1];

	  switch (action.type) {
	    case types.FETCH_GUESTS_NEW:
	    case types.FETCH_GUESTS_PENDING:
	    case types.FETCH_GUESTS_ERROR:
	      return (0, _extends3.default)({}, state, { status: action.type });
	    case types.FETCH_GUESTS_COMPLETED:
	      return (0, _assign2.default)({}, state, { items: action.guests, status: action.type });
	    // return { ...state, items: action.users, status: action.type };

	    default:
	      return state;
	  }
	};

	exports.default = guestsReducer;

/***/ }

});