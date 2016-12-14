webpackJsonp([3],{

/***/ 393:
/***/ function(module, exports, __webpack_require__) {

	module.exports = { "default": __webpack_require__(394), __esModule: true };

/***/ },

/***/ 394:
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(357);
	__webpack_require__(330);
	__webpack_require__(341);
	__webpack_require__(395);
	__webpack_require__(409);
	module.exports = __webpack_require__(7).Set;

/***/ },

/***/ 395:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var strong = __webpack_require__(396);

	// 23.2 Set Objects
	module.exports = __webpack_require__(405)('Set', function(get){
	  return function Set(){ return get(this, arguments.length > 0 ? arguments[0] : undefined); };
	}, {
	  // 23.2.3.1 Set.prototype.add(value)
	  add: function add(value){
	    return strong.def(this, value = value === 0 ? 0 : value, value);
	  }
	}, strong);

/***/ },

/***/ 396:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var dP          = __webpack_require__(11).f
	  , create      = __webpack_require__(337)
	  , redefineAll = __webpack_require__(397)
	  , ctx         = __webpack_require__(8)
	  , anInstance  = __webpack_require__(398)
	  , defined     = __webpack_require__(285)
	  , forOf       = __webpack_require__(399)
	  , $iterDefine = __webpack_require__(332)
	  , step        = __webpack_require__(344)
	  , setSpecies  = __webpack_require__(404)
	  , DESCRIPTORS = __webpack_require__(15)
	  , fastKey     = __webpack_require__(349).fastKey
	  , SIZE        = DESCRIPTORS ? '_s' : 'size';

	var getEntry = function(that, key){
	  // fast case
	  var index = fastKey(key), entry;
	  if(index !== 'F')return that._i[index];
	  // frozen object case
	  for(entry = that._f; entry; entry = entry.n){
	    if(entry.k == key)return entry;
	  }
	};

	module.exports = {
	  getConstructor: function(wrapper, NAME, IS_MAP, ADDER){
	    var C = wrapper(function(that, iterable){
	      anInstance(that, C, NAME, '_i');
	      that._i = create(null); // index
	      that._f = undefined;    // first entry
	      that._l = undefined;    // last entry
	      that[SIZE] = 0;         // size
	      if(iterable != undefined)forOf(iterable, IS_MAP, that[ADDER], that);
	    });
	    redefineAll(C.prototype, {
	      // 23.1.3.1 Map.prototype.clear()
	      // 23.2.3.2 Set.prototype.clear()
	      clear: function clear(){
	        for(var that = this, data = that._i, entry = that._f; entry; entry = entry.n){
	          entry.r = true;
	          if(entry.p)entry.p = entry.p.n = undefined;
	          delete data[entry.i];
	        }
	        that._f = that._l = undefined;
	        that[SIZE] = 0;
	      },
	      // 23.1.3.3 Map.prototype.delete(key)
	      // 23.2.3.4 Set.prototype.delete(value)
	      'delete': function(key){
	        var that  = this
	          , entry = getEntry(that, key);
	        if(entry){
	          var next = entry.n
	            , prev = entry.p;
	          delete that._i[entry.i];
	          entry.r = true;
	          if(prev)prev.n = next;
	          if(next)next.p = prev;
	          if(that._f == entry)that._f = next;
	          if(that._l == entry)that._l = prev;
	          that[SIZE]--;
	        } return !!entry;
	      },
	      // 23.2.3.6 Set.prototype.forEach(callbackfn, thisArg = undefined)
	      // 23.1.3.5 Map.prototype.forEach(callbackfn, thisArg = undefined)
	      forEach: function forEach(callbackfn /*, that = undefined */){
	        anInstance(this, C, 'forEach');
	        var f = ctx(callbackfn, arguments.length > 1 ? arguments[1] : undefined, 3)
	          , entry;
	        while(entry = entry ? entry.n : this._f){
	          f(entry.v, entry.k, this);
	          // revert to the last existing entry
	          while(entry && entry.r)entry = entry.p;
	        }
	      },
	      // 23.1.3.7 Map.prototype.has(key)
	      // 23.2.3.7 Set.prototype.has(value)
	      has: function has(key){
	        return !!getEntry(this, key);
	      }
	    });
	    if(DESCRIPTORS)dP(C.prototype, 'size', {
	      get: function(){
	        return defined(this[SIZE]);
	      }
	    });
	    return C;
	  },
	  def: function(that, key, value){
	    var entry = getEntry(that, key)
	      , prev, index;
	    // change existing entry
	    if(entry){
	      entry.v = value;
	    // create new entry
	    } else {
	      that._l = entry = {
	        i: index = fastKey(key, true), // <- index
	        k: key,                        // <- key
	        v: value,                      // <- value
	        p: prev = that._l,             // <- previous entry
	        n: undefined,                  // <- next entry
	        r: false                       // <- removed
	      };
	      if(!that._f)that._f = entry;
	      if(prev)prev.n = entry;
	      that[SIZE]++;
	      // add to index
	      if(index !== 'F')that._i[index] = entry;
	    } return that;
	  },
	  getEntry: getEntry,
	  setStrong: function(C, NAME, IS_MAP){
	    // add .keys, .values, .entries, [@@iterator]
	    // 23.1.3.4, 23.1.3.8, 23.1.3.11, 23.1.3.12, 23.2.3.5, 23.2.3.8, 23.2.3.10, 23.2.3.11
	    $iterDefine(C, NAME, function(iterated, kind){
	      this._t = iterated;  // target
	      this._k = kind;      // kind
	      this._l = undefined; // previous
	    }, function(){
	      var that  = this
	        , kind  = that._k
	        , entry = that._l;
	      // revert to the last existing entry
	      while(entry && entry.r)entry = entry.p;
	      // get next entry
	      if(!that._t || !(that._l = entry = entry ? entry.n : that._t._f)){
	        // or finish the iteration
	        that._t = undefined;
	        return step(1);
	      }
	      // return step by kind
	      if(kind == 'keys'  )return step(0, entry.k);
	      if(kind == 'values')return step(0, entry.v);
	      return step(0, [entry.k, entry.v]);
	    }, IS_MAP ? 'entries' : 'values' , !IS_MAP, true);

	    // add [@@species], 23.1.2.2, 23.2.2.2
	    setSpecies(NAME);
	  }
	};

/***/ },

/***/ 397:
/***/ function(module, exports, __webpack_require__) {

	var hide = __webpack_require__(10);
	module.exports = function(target, src, safe){
	  for(var key in src){
	    if(safe && target[key])target[key] = src[key];
	    else hide(target, key, src[key]);
	  } return target;
	};

/***/ },

/***/ 398:
/***/ function(module, exports) {

	module.exports = function(it, Constructor, name, forbiddenField){
	  if(!(it instanceof Constructor) || (forbiddenField !== undefined && forbiddenField in it)){
	    throw TypeError(name + ': incorrect invocation!');
	  } return it;
	};

/***/ },

/***/ 399:
/***/ function(module, exports, __webpack_require__) {

	var ctx         = __webpack_require__(8)
	  , call        = __webpack_require__(400)
	  , isArrayIter = __webpack_require__(401)
	  , anObject    = __webpack_require__(12)
	  , toLength    = __webpack_require__(287)
	  , getIterFn   = __webpack_require__(402)
	  , BREAK       = {}
	  , RETURN      = {};
	var exports = module.exports = function(iterable, entries, fn, that, ITERATOR){
	  var iterFn = ITERATOR ? function(){ return iterable; } : getIterFn(iterable)
	    , f      = ctx(fn, that, entries ? 2 : 1)
	    , index  = 0
	    , length, step, iterator, result;
	  if(typeof iterFn != 'function')throw TypeError(iterable + ' is not iterable!');
	  // fast case for arrays with default iterator
	  if(isArrayIter(iterFn))for(length = toLength(iterable.length); length > index; index++){
	    result = entries ? f(anObject(step = iterable[index])[0], step[1]) : f(iterable[index]);
	    if(result === BREAK || result === RETURN)return result;
	  } else for(iterator = iterFn.call(iterable); !(step = iterator.next()).done; ){
	    result = call(iterator, f, step.value, entries);
	    if(result === BREAK || result === RETURN)return result;
	  }
	};
	exports.BREAK  = BREAK;
	exports.RETURN = RETURN;

/***/ },

/***/ 400:
/***/ function(module, exports, __webpack_require__) {

	// call something on iterator step with safe closing on error
	var anObject = __webpack_require__(12);
	module.exports = function(iterator, fn, value, entries){
	  try {
	    return entries ? fn(anObject(value)[0], value[1]) : fn(value);
	  // 7.4.6 IteratorClose(iterator, completion)
	  } catch(e){
	    var ret = iterator['return'];
	    if(ret !== undefined)anObject(ret.call(iterator));
	    throw e;
	  }
	};

/***/ },

/***/ 401:
/***/ function(module, exports, __webpack_require__) {

	// check on default Array iterator
	var Iterators  = __webpack_require__(335)
	  , ITERATOR   = __webpack_require__(340)('iterator')
	  , ArrayProto = Array.prototype;

	module.exports = function(it){
	  return it !== undefined && (Iterators.Array === it || ArrayProto[ITERATOR] === it);
	};

/***/ },

/***/ 402:
/***/ function(module, exports, __webpack_require__) {

	var classof   = __webpack_require__(403)
	  , ITERATOR  = __webpack_require__(340)('iterator')
	  , Iterators = __webpack_require__(335);
	module.exports = __webpack_require__(7).getIteratorMethod = function(it){
	  if(it != undefined)return it[ITERATOR]
	    || it['@@iterator']
	    || Iterators[classof(it)];
	};

/***/ },

/***/ 403:
/***/ function(module, exports, __webpack_require__) {

	// getting tag from 19.1.3.6 Object.prototype.toString()
	var cof = __webpack_require__(23)
	  , TAG = __webpack_require__(340)('toStringTag')
	  // ES3 wrong here
	  , ARG = cof(function(){ return arguments; }()) == 'Arguments';

	// fallback for IE11 Script Access Denied error
	var tryGet = function(it, key){
	  try {
	    return it[key];
	  } catch(e){ /* empty */ }
	};

	module.exports = function(it){
	  var O, T, B;
	  return it === undefined ? 'Undefined' : it === null ? 'Null'
	    // @@toStringTag case
	    : typeof (T = tryGet(O = Object(it), TAG)) == 'string' ? T
	    // builtinTag case
	    : ARG ? cof(O)
	    // ES3 arguments fallback
	    : (B = cof(O)) == 'Object' && typeof O.callee == 'function' ? 'Arguments' : B;
	};

/***/ },

/***/ 404:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var global      = __webpack_require__(6)
	  , core        = __webpack_require__(7)
	  , dP          = __webpack_require__(11)
	  , DESCRIPTORS = __webpack_require__(15)
	  , SPECIES     = __webpack_require__(340)('species');

	module.exports = function(KEY){
	  var C = typeof core[KEY] == 'function' ? core[KEY] : global[KEY];
	  if(DESCRIPTORS && C && !C[SPECIES])dP.f(C, SPECIES, {
	    configurable: true,
	    get: function(){ return this; }
	  });
	};

/***/ },

/***/ 405:
/***/ function(module, exports, __webpack_require__) {

	'use strict';
	var global         = __webpack_require__(6)
	  , $export        = __webpack_require__(5)
	  , meta           = __webpack_require__(349)
	  , fails          = __webpack_require__(16)
	  , hide           = __webpack_require__(10)
	  , redefineAll    = __webpack_require__(397)
	  , forOf          = __webpack_require__(399)
	  , anInstance     = __webpack_require__(398)
	  , isObject       = __webpack_require__(13)
	  , setToStringTag = __webpack_require__(339)
	  , dP             = __webpack_require__(11).f
	  , each           = __webpack_require__(406)(0)
	  , DESCRIPTORS    = __webpack_require__(15);

	module.exports = function(NAME, wrapper, methods, common, IS_MAP, IS_WEAK){
	  var Base  = global[NAME]
	    , C     = Base
	    , ADDER = IS_MAP ? 'set' : 'add'
	    , proto = C && C.prototype
	    , O     = {};
	  if(!DESCRIPTORS || typeof C != 'function' || !(IS_WEAK || proto.forEach && !fails(function(){
	    new C().entries().next();
	  }))){
	    // create collection constructor
	    C = common.getConstructor(wrapper, NAME, IS_MAP, ADDER);
	    redefineAll(C.prototype, methods);
	    meta.NEED = true;
	  } else {
	    C = wrapper(function(target, iterable){
	      anInstance(target, C, NAME, '_c');
	      target._c = new Base;
	      if(iterable != undefined)forOf(iterable, IS_MAP, target[ADDER], target);
	    });
	    each('add,clear,delete,forEach,get,has,set,keys,values,entries,toJSON'.split(','),function(KEY){
	      var IS_ADDER = KEY == 'add' || KEY == 'set';
	      if(KEY in proto && !(IS_WEAK && KEY == 'clear'))hide(C.prototype, KEY, function(a, b){
	        anInstance(this, C, KEY);
	        if(!IS_ADDER && IS_WEAK && !isObject(a))return KEY == 'get' ? undefined : false;
	        var result = this._c[KEY](a === 0 ? 0 : a, b);
	        return IS_ADDER ? this : result;
	      });
	    });
	    if('size' in proto)dP(C.prototype, 'size', {
	      get: function(){
	        return this._c.size;
	      }
	    });
	  }

	  setToStringTag(C, NAME);

	  O[NAME] = C;
	  $export($export.G + $export.W + $export.F, O);

	  if(!IS_WEAK)common.setStrong(C, NAME, IS_MAP);

	  return C;
	};

/***/ },

/***/ 406:
/***/ function(module, exports, __webpack_require__) {

	// 0 -> Array#forEach
	// 1 -> Array#map
	// 2 -> Array#filter
	// 3 -> Array#some
	// 4 -> Array#every
	// 5 -> Array#find
	// 6 -> Array#findIndex
	var ctx      = __webpack_require__(8)
	  , IObject  = __webpack_require__(284)
	  , toObject = __webpack_require__(296)
	  , toLength = __webpack_require__(287)
	  , asc      = __webpack_require__(407);
	module.exports = function(TYPE, $create){
	  var IS_MAP        = TYPE == 1
	    , IS_FILTER     = TYPE == 2
	    , IS_SOME       = TYPE == 3
	    , IS_EVERY      = TYPE == 4
	    , IS_FIND_INDEX = TYPE == 6
	    , NO_HOLES      = TYPE == 5 || IS_FIND_INDEX
	    , create        = $create || asc;
	  return function($this, callbackfn, that){
	    var O      = toObject($this)
	      , self   = IObject(O)
	      , f      = ctx(callbackfn, that, 3)
	      , length = toLength(self.length)
	      , index  = 0
	      , result = IS_MAP ? create($this, length) : IS_FILTER ? create($this, 0) : undefined
	      , val, res;
	    for(;length > index; index++)if(NO_HOLES || index in self){
	      val = self[index];
	      res = f(val, index, O);
	      if(TYPE){
	        if(IS_MAP)result[index] = res;            // map
	        else if(res)switch(TYPE){
	          case 3: return true;                    // some
	          case 5: return val;                     // find
	          case 6: return index;                   // findIndex
	          case 2: result.push(val);               // filter
	        } else if(IS_EVERY)return false;          // every
	      }
	    }
	    return IS_FIND_INDEX ? -1 : IS_SOME || IS_EVERY ? IS_EVERY : result;
	  };
	};

/***/ },

/***/ 407:
/***/ function(module, exports, __webpack_require__) {

	// 9.4.2.3 ArraySpeciesCreate(originalArray, length)
	var speciesConstructor = __webpack_require__(408);

	module.exports = function(original, length){
	  return new (speciesConstructor(original))(length);
	};

/***/ },

/***/ 408:
/***/ function(module, exports, __webpack_require__) {

	var isObject = __webpack_require__(13)
	  , isArray  = __webpack_require__(353)
	  , SPECIES  = __webpack_require__(340)('species');

	module.exports = function(original){
	  var C;
	  if(isArray(original)){
	    C = original.constructor;
	    // cross-realm fallback
	    if(typeof C == 'function' && (C === Array || isArray(C.prototype)))C = undefined;
	    if(isObject(C)){
	      C = C[SPECIES];
	      if(C === null)C = undefined;
	    }
	  } return C === undefined ? Array : C;
	};

/***/ },

/***/ 409:
/***/ function(module, exports, __webpack_require__) {

	// https://github.com/DavidBruant/Map-Set.prototype.toJSON
	var $export  = __webpack_require__(5);

	$export($export.P + $export.R, 'Set', {toJSON: __webpack_require__(410)('Set')});

/***/ },

/***/ 410:
/***/ function(module, exports, __webpack_require__) {

	// https://github.com/DavidBruant/Map-Set.prototype.toJSON
	var classof = __webpack_require__(403)
	  , from    = __webpack_require__(411);
	module.exports = function(NAME){
	  return function toJSON(){
	    if(classof(this) != NAME)throw TypeError(NAME + "#toJSON isn't generic");
	    return from(this);
	  };
	};

/***/ },

/***/ 411:
/***/ function(module, exports, __webpack_require__) {

	var forOf = __webpack_require__(399);

	module.exports = function(iter, ITERATOR){
	  var result = [];
	  forOf(iter, false, result.push, result, ITERATOR);
	  return result;
	};


/***/ },

/***/ 412:
/***/ function(module, exports, __webpack_require__) {

	module.exports = { "default": __webpack_require__(413), __esModule: true };

/***/ },

/***/ 413:
/***/ function(module, exports, __webpack_require__) {

	__webpack_require__(414);
	module.exports = __webpack_require__(7).Object.keys;

/***/ },

/***/ 414:
/***/ function(module, exports, __webpack_require__) {

	// 19.1.2.14 Object.keys(O)
	var toObject = __webpack_require__(296)
	  , $keys    = __webpack_require__(280);

	__webpack_require__(320)('keys', function(){
	  return function keys(it){
	    return $keys(toObject(it));
	  };
	});

/***/ },

/***/ 549:
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

	__webpack_require__(550);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var SectionHeader = function (_Component) {
	  (0, _inherits3.default)(SectionHeader, _Component);

	  function SectionHeader() {
	    (0, _classCallCheck3.default)(this, SectionHeader);
	    return (0, _possibleConstructorReturn3.default)(this, (SectionHeader.__proto__ || (0, _getPrototypeOf2.default)(SectionHeader)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(SectionHeader, [{
	    key: 'render',
	    value: function render() {
	      return _react2.default.createElement(
	        'div',
	        { className: 'section-header' },
	        this.props.children
	      );
	    }
	  }]);
	  return SectionHeader;
	}(_react.Component);

	SectionHeader.propTypes = {
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = SectionHeader;

/***/ },

/***/ 550:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 551:
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

	__webpack_require__(552);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var SectionContent = function (_Component) {
	  (0, _inherits3.default)(SectionContent, _Component);

	  function SectionContent() {
	    (0, _classCallCheck3.default)(this, SectionContent);
	    return (0, _possibleConstructorReturn3.default)(this, (SectionContent.__proto__ || (0, _getPrototypeOf2.default)(SectionContent)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(SectionContent, [{
	    key: 'render',
	    value: function render() {
	      var overlayEnabled = this.props.overlayEnabled;


	      return _react2.default.createElement(
	        'div',
	        { className: 'section-content' },
	        this.props.children,
	        overlayEnabled && _react2.default.createElement(
	          'div',
	          { className: 'overlay' },
	          _react2.default.createElement(
	            'div',
	            { className: 'sk-circle' },
	            _react2.default.createElement('div', { className: 'sk-circle1 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle2 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle3 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle4 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle5 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle6 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle7 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle8 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle9 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle10 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle11 sk-child' }),
	            _react2.default.createElement('div', { className: 'sk-circle12 sk-child' })
	          )
	        )
	      );
	    }
	  }]);
	  return SectionContent;
	}(_react.Component);

	SectionContent.propTypes = {
	  overlayEnabled: _react.PropTypes.bool.isRequired,
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = SectionContent;

/***/ },

/***/ 552:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 586:
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

	var _SectionHeader = __webpack_require__(549);

	var _SectionHeader2 = _interopRequireDefault(_SectionHeader);

	var _SectionContent = __webpack_require__(551);

	var _SectionContent2 = _interopRequireDefault(_SectionContent);

	var _TabSwitcher = __webpack_require__(587);

	var _TabSwitcher2 = _interopRequireDefault(_TabSwitcher);

	var _TabSwitcherItem = __webpack_require__(589);

	var _TabSwitcherItem2 = _interopRequireDefault(_TabSwitcherItem);

	var _TaxiOrdersContainer = __webpack_require__(590);

	var _TaxiOrdersContainer2 = _interopRequireDefault(_TaxiOrdersContainer);

	var _TaxiNumbersContainer = __webpack_require__(596);

	var _TaxiNumbersContainer2 = _interopRequireDefault(_TaxiNumbersContainer);

	__webpack_require__(602);

	var _reactRouter = __webpack_require__(211);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var Taxi = function (_Component) {
	  (0, _inherits3.default)(Taxi, _Component);

	  function Taxi(props) {
	    (0, _classCallCheck3.default)(this, Taxi);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (Taxi.__proto__ || (0, _getPrototypeOf2.default)(Taxi)).call(this, props));

	    _this.handleTabSwitch = _this.handleTabSwitch.bind(_this);
	    _this.state = {
	      headers: [{
	        name: "Placed Orders",
	        id: "orders",
	        isActive: true,
	        component: _react2.default.createElement(_TaxiOrdersContainer2.default, null)
	      }, {
	        name: "Phone Number List",
	        id: "numbers",
	        isActive: false,
	        component: _react2.default.createElement(_TaxiNumbersContainer2.default, null)
	      }]
	    };
	    return _this;
	  }

	  (0, _createClass3.default)(Taxi, [{
	    key: 'handleTabSwitch',
	    value: function handleTabSwitch(headerName) {
	      this.state.headers.forEach(function (header) {
	        if (header.name === headerName) header.isActive = true;else header.isActive = false;
	      });

	      this.setState(this.state);
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(
	          _SectionHeader2.default,
	          null,
	          _react2.default.createElement(
	            'span',
	            null,
	            'Taxi service'
	          )
	        ),
	        _react2.default.createElement(
	          'div',
	          { className: 'taxi' },
	          _react2.default.createElement(_TabSwitcher2.default, { headers: this.state.headers, handleTabSwitch: this.handleTabSwitch })
	        )
	      );
	    }
	  }]);
	  return Taxi;
	}(_react.Component);

	exports.default = Taxi;


	Taxi.propTypes = {};

/***/ },

/***/ 587:
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

	__webpack_require__(588);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TabSwitcher = function (_Component) {
	  (0, _inherits3.default)(TabSwitcher, _Component);

	  function TabSwitcher() {
	    (0, _classCallCheck3.default)(this, TabSwitcher);
	    return (0, _possibleConstructorReturn3.default)(this, (TabSwitcher.__proto__ || (0, _getPrototypeOf2.default)(TabSwitcher)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(TabSwitcher, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          headers = _props.headers,
	          handleTabSwitch = _props.handleTabSwitch;


	      var headerComponents = [];
	      var itemComponents = [];

	      headers.forEach(function (header, index) {
	        if (header.isActive) {
	          var headerComponent = _react2.default.createElement(
	            'li',
	            { key: index, className: 'active',
	              onClick: function onClick(e) {
	                e.preventDefault();handleTabSwitch(header.name);
	              } },
	            _react2.default.createElement(
	              'a',
	              { href: "#" + header.id },
	              header.name
	            )
	          );

	          var itemComponent = _react2.default.createElement(
	            'div',
	            { key: index, id: header.id, className: 'tab-switcher-item tab-pane fade in active' },
	            header.component
	          );

	          headerComponents.push(headerComponent);
	          itemComponents.push(itemComponent);
	        } else {
	          var _headerComponent = _react2.default.createElement(
	            'li',
	            { key: index,
	              onClick: function onClick(e) {
	                e.preventDefault();handleTabSwitch(header.name);
	              } },
	            _react2.default.createElement(
	              'a',
	              { href: "#" + header.id },
	              header.name
	            )
	          );

	          var _itemComponent = _react2.default.createElement(
	            'div',
	            { key: index, id: header.id, className: 'tab-switcher-item tab-pane fade' },
	            header.component
	          );

	          headerComponents.push(_headerComponent);
	          itemComponents.push(_itemComponent);
	        }
	      });

	      return _react2.default.createElement(
	        'div',
	        { className: 'tab-switcher' },
	        _react2.default.createElement(
	          'ul',
	          { className: 'nav nav-tabs' },
	          headerComponents
	        ),
	        _react2.default.createElement(
	          'div',
	          { className: 'tab-content' },
	          itemComponents
	        )
	      );
	    }
	  }]);
	  return TabSwitcher;
	}(_react.Component);

	TabSwitcher.propTypes = {
	  headers: _react.PropTypes.array.isRequired,
	  handleTabSwitch: _react.PropTypes.func.isRequired
	};

	exports.default = TabSwitcher;

/***/ },

/***/ 588:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 589:
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

	__webpack_require__(588);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TabSwitcherItem = function (_Component) {
	  (0, _inherits3.default)(TabSwitcherItem, _Component);

	  function TabSwitcherItem() {
	    (0, _classCallCheck3.default)(this, TabSwitcherItem);
	    return (0, _possibleConstructorReturn3.default)(this, (TabSwitcherItem.__proto__ || (0, _getPrototypeOf2.default)(TabSwitcherItem)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(TabSwitcherItem, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          id = _props.id,
	          isActive = _props.isActive;


	      var activeClass = isActive ? " active" : "";

	      return _react2.default.createElement(
	        'div',
	        { id: id, className: "tab-switcher-item tab-pane fade in" + activeClass },
	        this.props.children
	      );
	    }
	  }]);
	  return TabSwitcherItem;
	}(_react.Component);

	TabSwitcherItem.propTypes = {
	  id: _react.PropTypes.string.isRequired,
	  isActive: _react.PropTypes.bool.isRequired,
	  children: _react.PropTypes.object.isRequired
	};

	exports.default = TabSwitcherItem;

/***/ },

/***/ 590:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _set = __webpack_require__(393);

	var _set2 = _interopRequireDefault(_set);

	var _keys = __webpack_require__(412);

	var _keys2 = _interopRequireDefault(_keys);

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

	var _TaxiOrders = __webpack_require__(591);

	var _TaxiOrders2 = _interopRequireDefault(_TaxiOrders);

	var _actions = __webpack_require__(560);

	var _actions2 = __webpack_require__(555);

	var types = _interopRequireWildcard(_actions2);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiOrdersContainer = function (_Component) {
	  (0, _inherits3.default)(TaxiOrdersContainer, _Component);

	  function TaxiOrdersContainer(props) {
	    (0, _classCallCheck3.default)(this, TaxiOrdersContainer);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (TaxiOrdersContainer.__proto__ || (0, _getPrototypeOf2.default)(TaxiOrdersContainer)).call(this, props));

	    _this.handleStatusChange = _this.handleStatusChange.bind(_this);
	    return _this;
	  }

	  (0, _createClass3.default)(TaxiOrdersContainer, [{
	    key: 'handleStatusChange',
	    value: function handleStatusChange(roomNumber, orderId, newStatus) {
	      var _this2 = this;

	      var stays = this.props.stays;


	      (0, _keys2.default)(stays.entries).forEach(function (pin) {
	        var stay = stays.entries[pin];
	        if (stay.roomNumber === roomNumber) {
	          _this2.props.dispatch((0, _actions.changeTaxiOrderStatus)(stay.pin, orderId, { status: newStatus }));
	        }
	      });
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          stays = _props.stays,
	          generic = _props.generic;


	      var orders = [];
	      (0, _keys2.default)(stays.entries).forEach(function (pin) {
	        var stay = stays.entries[pin];
	        if (stay.status === "ACTIVE") stay.orders.taxiOrders.forEach(function (order) {
	          var orderData = {
	            id: order.id,
	            roomNumber: stay.roomNumber,
	            time: order.time,
	            status: order.status
	          };
	          orders.push(orderData);
	        });
	      });

	      orders.sort(taxiOrdersComparator);

	      var pendingStatuses = new _set2.default([types.UPDATE_GENERIC_PENDING]);
	      var isPendingAction = pendingStatuses.has(generic.status);

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(_TaxiOrders2.default, { orders: orders,
	          handleStatusChange: this.handleStatusChange,
	          isPendingAction: isPendingAction
	        })
	      );
	    }
	  }]);
	  return TaxiOrdersContainer;
	}(_react.Component);

	var mapStateToProps = function mapStateToProps(state) {
	  return {
	    stays: state.stays,
	    generic: state.generic
	  };
	};

	TaxiOrdersContainer = (0, _reactRedux.connect)(mapStateToProps, null)(TaxiOrdersContainer);
	exports.default = TaxiOrdersContainer;


	var taxiOrdersComparator = function taxiOrdersComparator(orderA, orderB) {
	  if (orderA.id < orderB.id) return -1;else return 1;
	};

/***/ },

/***/ 591:
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

	var _TaxiOrdersEntry = __webpack_require__(592);

	var _TaxiOrdersEntry2 = _interopRequireDefault(_TaxiOrdersEntry);

	var _SectionContent = __webpack_require__(551);

	var _SectionContent2 = _interopRequireDefault(_SectionContent);

	__webpack_require__(595);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiOrders = function (_Component) {
	  (0, _inherits3.default)(TaxiOrders, _Component);

	  function TaxiOrders() {
	    (0, _classCallCheck3.default)(this, TaxiOrders);
	    return (0, _possibleConstructorReturn3.default)(this, (TaxiOrders.__proto__ || (0, _getPrototypeOf2.default)(TaxiOrders)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(TaxiOrders, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          orders = _props.orders,
	          handleStatusChange = _props.handleStatusChange,
	          isPendingAction = _props.isPendingAction;


	      var entries = orders.map(function (order, index) {
	        return _react2.default.createElement(_TaxiOrdersEntry2.default, { key: index + 1, order: order, handleStatusChange: handleStatusChange });
	      });

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(
	          _SectionContent2.default,
	          { overlayEnabled: isPendingAction },
	          _react2.default.createElement(
	            'div',
	            { className: 'row' },
	            _react2.default.createElement(
	              'div',
	              { className: 'taxi col-xs-offset-2 col-xs-8' },
	              _react2.default.createElement(
	                'table',
	                { className: 'table table-hover' },
	                _react2.default.createElement(
	                  'thead',
	                  null,
	                  _react2.default.createElement(
	                    'tr',
	                    null,
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-2' },
	                      'Room number'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-2' },
	                      'Time'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-2' },
	                      'Status'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-6' },
	                      'Actions'
	                    )
	                  )
	                ),
	                _react2.default.createElement(
	                  'tbody',
	                  null,
	                  entries
	                )
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return TaxiOrders;
	}(_react.Component);

	exports.default = TaxiOrders;


	TaxiOrders.propTypes = {
	  orders: _react.PropTypes.array.isRequired,
	  handleStatusChange: _react.PropTypes.func.isRequired,
	  isPendingAction: _react.PropTypes.bool.isRequired
	};

/***/ },

/***/ 592:
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

	var _OrderStatusChanger = __webpack_require__(593);

	var _OrderStatusChanger2 = _interopRequireDefault(_OrderStatusChanger);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiOrdersEntry = function (_Component) {
	  (0, _inherits3.default)(TaxiOrdersEntry, _Component);

	  function TaxiOrdersEntry(props) {
	    (0, _classCallCheck3.default)(this, TaxiOrdersEntry);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (TaxiOrdersEntry.__proto__ || (0, _getPrototypeOf2.default)(TaxiOrdersEntry)).call(this, props));

	    _this.handleStatusChange = _this.handleStatusChange.bind(_this);
	    return _this;
	  }

	  (0, _createClass3.default)(TaxiOrdersEntry, [{
	    key: 'handleStatusChange',
	    value: function handleStatusChange(newStatus) {
	      var order = this.props.order;


	      this.props.handleStatusChange(order.roomNumber, order.id, newStatus);
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var order = this.props.order;


	      return _react2.default.createElement(
	        'tr',
	        null,
	        _react2.default.createElement(
	          'td',
	          { className: 'col-md-2' },
	          order.roomNumber
	        ),
	        _react2.default.createElement(
	          'td',
	          { className: 'col-md-2' },
	          order.time
	        ),
	        _react2.default.createElement(
	          'td',
	          { className: 'col-md-2' },
	          order.status
	        ),
	        _react2.default.createElement(
	          'td',
	          { className: 'col-md-6' },
	          _react2.default.createElement(_OrderStatusChanger2.default, { status: order.status, handleStatusChange: this.handleStatusChange })
	        )
	      );
	    }
	  }]);
	  return TaxiOrdersEntry;
	}(_react.Component);

	exports.default = TaxiOrdersEntry;


	TaxiOrdersEntry.propTypes = {
	  order: _react.PropTypes.object.isRequired,
	  handleStatusChange: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 593:
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

	var _orderStates = __webpack_require__(594);

	var states = _interopRequireWildcard(_orderStates);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var OrderStatusChanger = function (_Component) {
	  (0, _inherits3.default)(OrderStatusChanger, _Component);

	  function OrderStatusChanger() {
	    (0, _classCallCheck3.default)(this, OrderStatusChanger);
	    return (0, _possibleConstructorReturn3.default)(this, (OrderStatusChanger.__proto__ || (0, _getPrototypeOf2.default)(OrderStatusChanger)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(OrderStatusChanger, [{
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          status = _props.status,
	          handleStatusChange = _props.handleStatusChange;


	      var accept = void 0,
	          cancelOrComplete = void 0,
	          reset = void 0;
	      accept = cancelOrComplete = reset = "";

	      if (status === states.NEW || status === states.PENDING) accept = " active";
	      if (status !== states.CANCELLED && status !== states.COMPLETED) cancelOrComplete = " active";
	      if (status !== states.NEW && status !== states.PENDING) reset = " active";

	      return _react2.default.createElement(
	        'div',
	        { className: 'order-status-changer' },
	        _react2.default.createElement(
	          'div',
	          { className: "col-xs-3" + accept },
	          _react2.default.createElement(
	            'a',
	            { href: '#', onClick: function onClick(event) {
	                event.preventDefault();handleStatusChange(states.ACCEPTED);
	              } },
	            _react2.default.createElement('i', { className: 'fa fa-thumbs-up fa-lg', 'aria-hidden': 'true' }),
	            _react2.default.createElement(
	              'div',
	              null,
	              'accept'
	            )
	          )
	        ),
	        _react2.default.createElement(
	          'div',
	          { className: "col-xs-3" + cancelOrComplete },
	          _react2.default.createElement(
	            'a',
	            { href: '#', onClick: function onClick(event) {
	                event.preventDefault();handleStatusChange(states.COMPLETED);
	              } },
	            _react2.default.createElement('i', { className: 'fa fa-check fa-lg', 'aria-hidden': 'true' }),
	            _react2.default.createElement(
	              'div',
	              null,
	              'complete'
	            )
	          )
	        ),
	        _react2.default.createElement(
	          'div',
	          { className: "col-xs-3" + cancelOrComplete },
	          _react2.default.createElement(
	            'a',
	            { href: '#', onClick: function onClick(event) {
	                event.preventDefault();handleStatusChange(states.CANCELLED);
	              } },
	            _react2.default.createElement('i', { className: 'fa fa-times fa-lg', 'aria-hidden': 'true' }),
	            _react2.default.createElement(
	              'div',
	              null,
	              'cancel'
	            )
	          )
	        ),
	        _react2.default.createElement(
	          'div',
	          { className: "col-xs-3" + reset },
	          _react2.default.createElement(
	            'a',
	            { href: '#', onClick: function onClick(event) {
	                event.preventDefault();handleStatusChange(states.PENDING);
	              } },
	            _react2.default.createElement('i', { className: 'fa fa-repeat fa-lg', 'aria-hidden': 'true' }),
	            _react2.default.createElement(
	              'div',
	              null,
	              'reset'
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return OrderStatusChanger;
	}(_react.Component);

	OrderStatusChanger.propTypes = {
	  status: _react.PropTypes.string.isRequired,
	  handleStatusChange: _react.PropTypes.func.isRequired
	};

	exports.default = OrderStatusChanger;

/***/ },

/***/ 594:
/***/ function(module, exports) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});
	var NEW = exports.NEW = 'NEW';
	var PENDING = exports.PENDING = 'PENDING';
	var ACCEPTED = exports.ACCEPTED = 'ACCEPTED';
	var COMPLETED = exports.COMPLETED = 'COMPLETED';
	var CANCELLED = exports.CANCELLED = 'CANCELLED';

/***/ },

/***/ 595:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 596:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _set = __webpack_require__(393);

	var _set2 = _interopRequireDefault(_set);

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

	var _TaxiNumbers = __webpack_require__(597);

	var _TaxiNumbers2 = _interopRequireDefault(_TaxiNumbers);

	var _actions = __webpack_require__(555);

	var types = _interopRequireWildcard(_actions);

	var _actions2 = __webpack_require__(560);

	var taxiTypes = _interopRequireWildcard(_actions2);

	var _config = __webpack_require__(388);

	var _config2 = _interopRequireDefault(_config);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiNumbersContainer = function (_Component) {
	  (0, _inherits3.default)(TaxiNumbersContainer, _Component);

	  function TaxiNumbersContainer(props) {
	    (0, _classCallCheck3.default)(this, TaxiNumbersContainer);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (TaxiNumbersContainer.__proto__ || (0, _getPrototypeOf2.default)(TaxiNumbersContainer)).call(this, props));

	    _this.handleAddTaxiNumber = _this.handleAddTaxiNumber.bind(_this);
	    _this.handleUpdateTaxiNumber = _this.handleUpdateTaxiNumber.bind(_this);
	    _this.handleDeleteTaxiNumber = _this.handleDeleteTaxiNumber.bind(_this);
	    return _this;
	  }

	  (0, _createClass3.default)(TaxiNumbersContainer, [{
	    key: 'componentDidMount',
	    value: function componentDidMount() {
	      var _this2 = this;

	      this.props.dispatch(taxiTypes.fetchTaxiNumbers());
	      setInterval(function () {
	        return _this2.props.dispatch(taxiTypes.fetchTaxiNumbers());
	      }, _config2.default.refreshInterval);
	    }
	  }, {
	    key: 'handleAddTaxiNumber',
	    value: function handleAddTaxiNumber(name, number) {
	      var entity = {
	        name: name,
	        number: number
	      };
	      this.props.dispatch(taxiTypes.addTaxiNumber(entity));
	    }
	  }, {
	    key: 'handleUpdateTaxiNumber',
	    value: function handleUpdateTaxiNumber(id, name, number) {
	      var entity = {
	        id: id,
	        name: name,
	        number: number
	      };
	      this.props.dispatch(taxiTypes.updateTaxiNumber(id, entity));
	    }
	  }, {
	    key: 'handleDeleteTaxiNumber',
	    value: function handleDeleteTaxiNumber(id) {
	      this.props.dispatch(taxiTypes.deleteTaxiNumber(id));
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _props = this.props,
	          taxiNumbers = _props.taxiNumbers,
	          generic = _props.generic;

	      var items = taxiNumbers.items.sort(byNameComparator);

	      var pendingStatuses = new _set2.default([types.UPDATE_GENERIC_PENDING, types.POST_GENERIC_PENDING, types.DELETE_GENERIC_PENDING]);
	      var isPendingAction = pendingStatuses.has(generic.status);

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(_TaxiNumbers2.default, { items: items,
	          handleAddTaxiNumber: this.handleAddTaxiNumber,
	          handleUpdateTaxiNumber: this.handleUpdateTaxiNumber,
	          handleDeleteTaxiNumber: this.handleDeleteTaxiNumber,
	          isPendingAction: isPendingAction
	        })
	      );
	    }
	  }]);
	  return TaxiNumbersContainer;
	}(_react.Component);

	var mapStateToProps = function mapStateToProps(state) {
	  return {
	    taxiNumbers: state.taxiNumbers,
	    generic: state.generic
	  };
	};

	TaxiNumbersContainer = (0, _reactRedux.connect)(mapStateToProps, null)(TaxiNumbersContainer);
	exports.default = TaxiNumbersContainer;


	var byNameComparator = function byNameComparator(itemA, itemB) {
	  if (itemA.name < itemB.name) return -1;else if (itemA.name > itemB.name) return 1;else {
	    // shouldn't happen as there should be no duplicates
	    if (itemA.number < itemB.number) return -1;else if (itemA.number > itemB.number) return 1;else return 0;
	  }
	};

/***/ },

/***/ 597:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _assign = __webpack_require__(276);

	var _assign2 = _interopRequireDefault(_assign);

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

	var _SectionContent = __webpack_require__(551);

	var _SectionContent2 = _interopRequireDefault(_SectionContent);

	var _TaxiNumbersEntry = __webpack_require__(598);

	var _TaxiNumbersEntry2 = _interopRequireDefault(_TaxiNumbersEntry);

	var _TaxiNumbersSave = __webpack_require__(599);

	var _TaxiNumbersSave2 = _interopRequireDefault(_TaxiNumbersSave);

	__webpack_require__(601);

	var _jquery = __webpack_require__(389);

	var _jquery2 = _interopRequireDefault(_jquery);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiNumbers = function (_Component) {
	  (0, _inherits3.default)(TaxiNumbers, _Component);

	  function TaxiNumbers(props) {
	    (0, _classCallCheck3.default)(this, TaxiNumbers);

	    var _this = (0, _possibleConstructorReturn3.default)(this, (TaxiNumbers.__proto__ || (0, _getPrototypeOf2.default)(TaxiNumbers)).call(this, props));

	    _this.handleNewTaxiNumber = _this.handleNewTaxiNumber.bind(_this);
	    _this.handleEditTaxiNumber = _this.handleEditTaxiNumber.bind(_this);
	    _this.handleDeleteTaxiNumber = _this.handleDeleteTaxiNumber.bind(_this);
	    _this.handleSaveTaxiNumber = _this.handleSaveTaxiNumber.bind(_this);
	    _this.handleCancel = _this.handleCancel.bind(_this);
	    _this.state = {
	      currentId: -1,
	      title: "Adding a new taxi entry",
	      name: "",
	      number: "",
	      modalOpen: false
	    };
	    return _this;
	  }

	  (0, _createClass3.default)(TaxiNumbers, [{
	    key: 'componentDidMount',
	    value: function componentDidMount() {
	      var _this2 = this;

	      (0, _jquery2.default)('#taxiNumbersModal').on('shown.bs.modal', function (e) {
	        _this2.setState((0, _assign2.default)({}, _this2.state, { modalOpen: true }));
	      });

	      (0, _jquery2.default)('#taxiNumbersModal').on('hidden.bs.modal', function (e) {
	        _this2.setState((0, _assign2.default)({}, _this2.state, { modalOpen: false }));
	      });
	    }
	  }, {
	    key: 'handleCancel',
	    value: function handleCancel() {
	      (0, _jquery2.default)("#taxiNumbersModal").modal('hide');
	    }
	  }, {
	    key: 'handleNewTaxiNumber',
	    value: function handleNewTaxiNumber() {
	      this.setState({
	        currentId: -1,
	        title: "Adding a new taxi entry",
	        name: "",
	        number: ""
	      });

	      (0, _jquery2.default)("#taxiNumbersModal").find('input').val('');
	      (0, _jquery2.default)("#taxiNumbersModal").modal('show');
	    }
	  }, {
	    key: 'handleEditTaxiNumber',
	    value: function handleEditTaxiNumber(itemId) {
	      var name = void 0,
	          number = void 0;
	      this.props.items.forEach(function (item) {
	        if (item.id === itemId) {
	          name = item.name;
	          number = item.number;
	        }
	      });

	      this.setState({
	        currentId: itemId,
	        title: "Changing the existing entry",
	        name: name,
	        number: number
	      });

	      (0, _jquery2.default)("#taxiNumbersModal").find('input').val('');
	      (0, _jquery2.default)("#taxiNumbersModal").modal('show');
	    }
	  }, {
	    key: 'handleDeleteTaxiNumber',
	    value: function handleDeleteTaxiNumber(itemId) {
	      this.props.handleDeleteTaxiNumber(itemId);
	    }
	  }, {
	    key: 'handleSaveTaxiNumber',
	    value: function handleSaveTaxiNumber(itemId, name, number) {
	      (0, _jquery2.default)("#taxiNumbersModal").modal('hide');

	      if (itemId === -1) this.props.handleAddTaxiNumber(name, number);else this.props.handleUpdateTaxiNumber(itemId, name, number);
	    }
	  }, {
	    key: 'render',
	    value: function render() {
	      var _this3 = this;

	      var _props = this.props,
	          items = _props.items,
	          isPendingAction = _props.isPendingAction;


	      var entries = items.map(function (item, index) {
	        return _react2.default.createElement(_TaxiNumbersEntry2.default, { key: index + 1,
	          nr: index + 1,
	          item: item,
	          handleEditTaxiNumber: _this3.handleEditTaxiNumber,
	          handleDeleteTaxiNumber: _this3.handleDeleteTaxiNumber
	        });
	      });

	      return _react2.default.createElement(
	        'div',
	        null,
	        _react2.default.createElement(
	          _SectionContent2.default,
	          { overlayEnabled: isPendingAction },
	          _react2.default.createElement(
	            'div',
	            { className: 'row' },
	            _react2.default.createElement(
	              'div',
	              { className: 'taxi-numbers col-xs-offset-3 col-xs-6' },
	              _react2.default.createElement(
	                'button',
	                { onClick: function onClick(event) {
	                    event.preventDefault();_this3.handleNewTaxiNumber();
	                  },
	                  type: 'button', className: 'btn btn-primary pull-right', 'data-toggle': 'modal' },
	                'Add'
	              ),
	              _react2.default.createElement(
	                'table',
	                { className: 'table table-hover' },
	                _react2.default.createElement(
	                  'thead',
	                  null,
	                  _react2.default.createElement(
	                    'tr',
	                    null,
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-1' },
	                      '#'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-4' },
	                      'Name'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-4' },
	                      'Phone number'
	                    ),
	                    _react2.default.createElement(
	                      'th',
	                      { className: 'col-md-3' },
	                      'Actions'
	                    )
	                  )
	                ),
	                _react2.default.createElement(
	                  'tbody',
	                  null,
	                  entries
	                )
	              )
	            )
	          )
	        ),
	        _react2.default.createElement(_TaxiNumbersSave2.default, { id: this.state.currentId,
	          title: this.state.title,
	          name: this.state.name,
	          number: this.state.number,
	          modalOpen: this.state.modalOpen,
	          handleCancel: this.handleCancel,
	          handleSaveTaxiNumber: this.handleSaveTaxiNumber
	        })
	      );
	    }
	  }]);
	  return TaxiNumbers;
	}(_react.Component);

	exports.default = TaxiNumbers;


	TaxiNumbers.propTypes = {
	  items: _react.PropTypes.array.isRequired,
	  isPendingAction: _react.PropTypes.bool.isRequired,
	  handleAddTaxiNumber: _react.PropTypes.func.isRequired,
	  handleUpdateTaxiNumber: _react.PropTypes.func.isRequired,
	  handleDeleteTaxiNumber: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 598:
/***/ function(module, exports, __webpack_require__) {

	"use strict";

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

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiNumbersEntry = function (_Component) {
	  (0, _inherits3.default)(TaxiNumbersEntry, _Component);

	  function TaxiNumbersEntry() {
	    (0, _classCallCheck3.default)(this, TaxiNumbersEntry);
	    return (0, _possibleConstructorReturn3.default)(this, (TaxiNumbersEntry.__proto__ || (0, _getPrototypeOf2.default)(TaxiNumbersEntry)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(TaxiNumbersEntry, [{
	    key: "render",
	    value: function render() {
	      var _props = this.props,
	          nr = _props.nr,
	          item = _props.item,
	          handleEditTaxiNumber = _props.handleEditTaxiNumber,
	          handleDeleteTaxiNumber = _props.handleDeleteTaxiNumber;


	      return _react2.default.createElement(
	        "tr",
	        null,
	        _react2.default.createElement(
	          "td",
	          { className: "col-md-1" },
	          nr
	        ),
	        _react2.default.createElement(
	          "td",
	          { className: "col-md-4" },
	          item.name
	        ),
	        _react2.default.createElement(
	          "td",
	          { className: "col-md-4" },
	          item.number
	        ),
	        _react2.default.createElement(
	          "td",
	          { className: "col-md-3" },
	          _react2.default.createElement(
	            "div",
	            { className: "col-xs-6 active" },
	            _react2.default.createElement(
	              "a",
	              { href: "#", onClick: function onClick(e) {
	                  e.preventDefault();handleEditTaxiNumber(item.id);
	                } },
	              _react2.default.createElement("i", { className: "fa fa-pencil fa-lg", "aria-hidden": "true" }),
	              _react2.default.createElement(
	                "div",
	                null,
	                "edit"
	              )
	            )
	          ),
	          _react2.default.createElement(
	            "div",
	            { className: "col-xs-6 active" },
	            _react2.default.createElement(
	              "a",
	              { href: "#", onClick: function onClick(e) {
	                  e.preventDefault();handleDeleteTaxiNumber(item.id);
	                } },
	              _react2.default.createElement("i", { className: "fa fa-trash-o fa-lg", "aria-hidden": "true" }),
	              _react2.default.createElement(
	                "div",
	                null,
	                "delete"
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return TaxiNumbersEntry;
	}(_react.Component);

	exports.default = TaxiNumbersEntry;


	TaxiNumbersEntry.propTypes = {
	  nr: _react.PropTypes.number,
	  item: _react.PropTypes.object.isRequired,
	  handleEditTaxiNumber: _react.PropTypes.func.isRequired,
	  handleDeleteTaxiNumber: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 599:
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

	var _jquery = __webpack_require__(389);

	var _jquery2 = _interopRequireDefault(_jquery);

	__webpack_require__(600);

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var TaxiNumbersSave = function (_Component) {
	  (0, _inherits3.default)(TaxiNumbersSave, _Component);

	  function TaxiNumbersSave() {
	    (0, _classCallCheck3.default)(this, TaxiNumbersSave);
	    return (0, _possibleConstructorReturn3.default)(this, (TaxiNumbersSave.__proto__ || (0, _getPrototypeOf2.default)(TaxiNumbersSave)).apply(this, arguments));
	  }

	  (0, _createClass3.default)(TaxiNumbersSave, [{
	    key: 'render',
	    value: function render() {
	      var _this2 = this;

	      var _props = this.props,
	          id = _props.id,
	          title = _props.title,
	          name = _props.name,
	          number = _props.number,
	          modalOpen = _props.modalOpen,
	          handleCancel = _props.handleCancel,
	          handleSaveTaxiNumber = _props.handleSaveTaxiNumber;


	      if (!modalOpen) {
	        (0, _jquery2.default)("#name").val(name);
	        (0, _jquery2.default)("#phoneNumber").val(number);
	      }

	      return _react2.default.createElement(
	        'div',
	        { id: 'taxiNumbersModal', className: 'taxi-numbers-save modal fade', tabIndex: '-1', role: 'dialog' },
	        _react2.default.createElement(
	          'div',
	          { className: 'modal-dialog', role: 'document' },
	          _react2.default.createElement(
	            'div',
	            { className: 'modal-content' },
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-header' },
	              _react2.default.createElement(
	                'h4',
	                { className: 'modal-title' },
	                title
	              )
	            ),
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-body' },
	              _react2.default.createElement(
	                'form',
	                null,
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'name' },
	                    'Name'
	                  ),
	                  _react2.default.createElement('input', { type: 'text', className: 'form-control', id: 'name', ref: 'nameRef' })
	                ),
	                _react2.default.createElement(
	                  'div',
	                  { className: 'form-group' },
	                  _react2.default.createElement(
	                    'label',
	                    { htmlFor: 'phoneNumber' },
	                    'Phone number'
	                  ),
	                  _react2.default.createElement('input', { type: 'text', className: 'form-control', id: 'phoneNumber', ref: 'phoneNumberRef' })
	                )
	              )
	            ),
	            _react2.default.createElement(
	              'div',
	              { className: 'modal-footer' },
	              _react2.default.createElement(
	                'button',
	                { onClick: function onClick(event) {
	                    event.preventDefault();handleCancel();
	                  },
	                  type: 'button', className: 'btn btn-default' },
	                'Cancel'
	              ),
	              _react2.default.createElement(
	                'button',
	                { onClick: function onClick(event) {
	                    event.preventDefault();
	                    handleSaveTaxiNumber(id, _this2.refs.nameRef.value, _this2.refs.phoneNumberRef.value);
	                  },
	                  type: 'button', className: 'btn btn-primary' },
	                'Save'
	              )
	            )
	          )
	        )
	      );
	    }
	  }]);
	  return TaxiNumbersSave;
	}(_react.Component);

	exports.default = TaxiNumbersSave;


	TaxiNumbersSave.propTypes = {
	  id: _react.PropTypes.number.isRequired,
	  title: _react.PropTypes.string.isRequired,
	  name: _react.PropTypes.string.isRequired,
	  number: _react.PropTypes.string.isRequired,
	  modalOpen: _react.PropTypes.bool.isRequired,
	  handleSaveTaxiNumber: _react.PropTypes.func.isRequired,
	  handleCancel: _react.PropTypes.func.isRequired
	};

/***/ },

/***/ 600:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 601:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 602:
/***/ function(module, exports) {

	// removed by extract-text-webpack-plugin

/***/ },

/***/ 603:
/***/ function(module, exports, __webpack_require__) {

	'use strict';

	Object.defineProperty(exports, "__esModule", {
	  value: true
	});

	var _extends2 = __webpack_require__(275);

	var _extends3 = _interopRequireDefault(_extends2);

	var _actions = __webpack_require__(560);

	var types = _interopRequireWildcard(_actions);

	function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

	function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

	var taxiNumbersReducer = function taxiNumbersReducer() {
	  var state = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { items: [], status: types.FETCH_TAXI_NUMBERS_NEW };
	  var action = arguments[1];

	  switch (action.type) {
	    case types.FETCH_TAXI_NUMBERS_NEW:
	    case types.FETCH_TAXI_NUMBERS_PENDING:
	    case types.FETCH_TAXI_NUMBERS_ERROR:
	      return (0, _extends3.default)({}, state, { status: action.type });
	    case types.FETCH_TAXI_NUMBERS_COMPLETED:
	      return (0, _extends3.default)({}, state, action.taxiNumbers, { status: action.type });

	    default:
	      return state;
	  }
	};

	exports.default = taxiNumbersReducer;

/***/ }

});