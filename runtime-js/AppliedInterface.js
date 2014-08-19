function AppliedInterface(tipo,$$targs$$,that,myTargs) {
  $init$AppliedInterface();
  if (that===undefined){
    var mm = getrtmm$$(tipo);
    if (mm && mm.$cont) {
      that=function(x){
        that.tipo=function(){return tipo.apply(x,arguments);};
        that.$bound=x;
        return that;
      }
      that.tipo$2=tipo;
      that.getMethod=ClassOrInterface$meta$model.$$.prototype.getMethod;
      that.getDeclaredMethod=ClassOrInterface$meta$model.$$.prototype.getDeclaredMethod;
      that.getAttribute=ClassOrInterface$meta$model.$$.prototype.getAttribute;
      that.getDeclaredAttribute=ClassOrInterface$meta$model.$$.prototype.getDeclaredAttribute;
      that.getClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getClassOrInterface;
      that.getDeclaredClassOrInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredClassOrInterface;
      that.getClass=ClassOrInterface$meta$model.$$.prototype.getClass;
      that.getDeclaredClass=ClassOrInterface$meta$model.$$.prototype.getDeclaredClass;
      that.getInterface=ClassOrInterface$meta$model.$$.prototype.getInterface;
      that.getDeclaredInterface=ClassOrInterface$meta$model.$$.prototype.getDeclaredInterface;
      that.typeOf=ClassOrInterface$meta$model.$$.prototype.typeOf;
      that.supertypeOf=ClassOrInterface$meta$model.$$.prototype.supertypeOf;
      that.subtypeOf=ClassOrInterface$meta$model.$$.prototype.subtypeOf;
      that.exactly=ClassOrInterface$meta$model.$$.prototype.exactly;
      var dummy = new AppliedInterface.$$;
      that.$$=AppliedInterface.$$;
      that.getT$all=function(){return dummy.getT$all();};
      that.getT$name=function(){return dummy.getT$name();};
      atr$(that,'typeArguments',function(){
        return ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.get.call(that);
      },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getTypeArguments.$crtmm$);
      atr$(that,'string',function(){return coistr$(that); },undefined,$_Object({}).$prop$getString.$crtmm$);
      atr$(that,'hash',function(){return coihash$(that);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
      atr$(that,'declaration',function(){
        return coimoddcl$(that);
      },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
      atr$(that,'container',function(){return coicont$(that); },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
    } else {
      that=new AppliedInterface.$$;
    }
  }
  set_type_args(that,$$targs$$);
  Interface$meta$model($$targs$$,that);
  that.$targs=myTargs;
  that.equals=function(o){
    var eq=is$(o,{t:AppliedInterface}) && (o.tipo$2||o.tipo)==tipo;
    if (that.$bound)eq=eq && o.$bound && o.$bound.equals(that.$bound);else eq=eq && o.$bound===undefined;
    return eq && this.typeArguments.equals(o.typeArguments);
  };
  that.tipo=tipo;
  return that;
}
AppliedInterface.$crtmm$=function(){return{mod:$CCMM$,'super':{t:Basic},tp:{Type$Interface:{dv:'out','def':{t:Anything}}},sts:[{t:Interface$meta$model,a:{Type$Interface:'Type$Interface'}}],pa:1,d:['ceylon.language.meta.model','Interface']};};
ex$.AppliedInterface=AppliedInterface;

function $init$AppliedInterface(){
  if (AppliedInterface.$$===undefined){
    initTypeProto(AppliedInterface,'ceylon.language.meta.model::AppliedInterface',Basic,Interface$meta$model);
    var aip$=AppliedInterface.$$.prototype;
    atr$(aip$,'string',function(){return coistr$(this); },undefined,$_Object({}).$prop$getString.$crtmm$);
    atr$(aip$,'hash',function(){return coihash$(this);},undefined,Identifiable.$$.prototype.$prop$getHash.$crtmm$);
    atr$(aip$,'declaration',function(){
      return coimoddcl$(this);
    },undefined,InterfaceModel$meta$model.$$.prototype.$prop$getDeclaration.$crtmm$);
    atr$(aip$,'container',function(){return coicont$(this); },undefined,ClassOrInterface$meta$model.$$.prototype.$prop$getContainer.$crtmm$);
  }
  return AppliedInterface;
}
ex$.$init$AppliedInterface$meta$model=$init$AppliedInterface;
$init$AppliedInterface();
