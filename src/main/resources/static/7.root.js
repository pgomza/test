webpackJsonp([7],{

/***/ 660:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(358);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(361);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(362);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(366);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(382);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _reactRedux = __webpack_require__(390);

	var _actions = __webpack_require__(546);

	var _guestActions = __webpack_require__(548);

	var _MainPanel = __webpack_require__(661);

	var _MainPanel2 = _interopRequireDefault(_MainPanel);

	var _MainHeader = __webpack_require__(663);

	var _MainHeader2 = _interopRequireDefault(_MainHeader);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var RootContainer = function (_Component) {
	  (0, _inherits3.default)(RootContainer, _Component);

	  function RootContainer() {
	    (0, _classCallCheck3.default)(this, RootContainer);
	    return (0, _possibleConstructorReturn3.default)(this, (RootContainer.__proto__ || (0, _getPrototypeOf2.default)(RootContainer)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(RootContainer, [{
	    key: 'componentDidMount',
	    value: function componentDidMount() {
	      var _this2 = this;

	      this.props.dispatch((0, _actions.fetchStays)());
	      this.props.dispatch((0, _guestActions.fetchGuests)());
	      setInterval(function () {
	        _this2.props.dispatch((0, _actions.fetchStays)());
	      }, 5000);
	      setInterval(function () {
	        _this2.props.dispatch((0, _guestActions.fetchGuests)());
	      }, 5000);
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(_MainHeader2.default, null),
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
	    location: state.location
	  };
	};

	RootContainer = (0, _reactRedux.connect)(mapStateToProps, null)(RootContainer);
	exports.default = RootContainer;

/***/ },

/***/ 661:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(358);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(361);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(362);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(366);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(382);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	__webpack_require__(662);

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

/***/ 662:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 663:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _getPrototypeOf = __webpack_require__(358);

	var _getPrototypeOf2 = _interopRequireDefault(_getPrototypeOf);

	var _classCallCheck2 = __webpack_require__(361);

	var _classCallCheck3 = _interopRequireDefault(_classCallCheck2);

	var _createClass2 = __webpack_require__(362);

	var _createClass3 = _interopRequireDefault(_createClass2);

	var _possibleConstructorReturn2 = __webpack_require__(366);

	var _possibleConstructorReturn3 = _interopRequireDefault(_possibleConstructorReturn2);

	var _inherits2 = __webpack_require__(382);

	var _inherits3 = _interopRequireDefault(_inherits2);

	var _react = __webpack_require__(24);

	var _react2 = _interopRequireDefault(_react);

	var _reactRouter = __webpack_require__(211);

	__webpack_require__(664);

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
	      return _react2.default.createElement(
	        'nav',
	        { className: 'navbar navbar-default main-header' },
	        _react2.default.createElement(
	          'div',
	          { className: 'container' },
	          _react2.default.createElement(
	            'ul',
	            { className: 'nav navbar-nav navbar-right' },
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
	          )
	        )
	      );
	    }
	  }]);
	  return MainHeader;
	}(_react.Component);

	exports.default = MainHeader;

/***/ },

/***/ 664:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 665:
/***/ function(module, exports, __webpack_require__) {

	"use strict";

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _actions = __webpack_require__(546);

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

/***/ 666:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _typeof2 = __webpack_require__(367);

	var _typeof3 = _interopRequireDefault(_typeof2);

	var _assign = __webpack_require__(276);

	var _assign2 = _interopRequireDefault(_assign);

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _actions = __webpack_require__(546);

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

/***/ 667:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _assign = __webpack_require__(276);

	var _assign2 = _interopRequireDefault(_assign);

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _guestActions = __webpack_require__(548);

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