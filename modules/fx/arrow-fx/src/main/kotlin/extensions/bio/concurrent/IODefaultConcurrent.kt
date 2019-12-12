package arrow.fx.extensions.bio.concurrent

import arrow.Kind
import arrow.core.Either
import arrow.fx.BIO
import arrow.fx.BIO.Companion
import arrow.fx.BIOPartialOf
import arrow.fx.IO
import arrow.fx.MVar
import arrow.fx.Promise
import arrow.fx.Race3
import arrow.fx.Race4
import arrow.fx.Race5
import arrow.fx.Race6
import arrow.fx.Race7
import arrow.fx.Race8
import arrow.fx.Race9
import arrow.fx.Semaphore
import arrow.fx.Timer
import arrow.fx.extensions.IODefaultConcurrent
import arrow.fx.typeclasses.ConcurrentContinuation
import arrow.fx.typeclasses.Dispatchers
import arrow.fx.typeclasses.Duration
import arrow.fx.typeclasses.Fiber
import arrow.typeclasses.Applicative
import arrow.typeclasses.Traverse
import kotlin.Function1
import kotlin.Function2
import kotlin.Function3
import kotlin.Function4
import kotlin.Function5
import kotlin.Function6
import kotlin.Function7
import kotlin.Function8
import kotlin.Function9
import kotlin.Long
import kotlin.PublishedApi
import kotlin.Suppress
import kotlin.Throwable
import kotlin.Unit
import kotlin.collections.Iterable
import kotlin.collections.List
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

/**
 * cached extension
 */
@PublishedApi()
internal val concurrent_singleton: IODefaultConcurrent = object :
  arrow.fx.extensions.IODefaultConcurrent {}

@JvmName("timer")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun timer(): Timer<BIOPartialOf<Nothing>> = arrow.fx.BIO
  .concurrent()
  .timer() as arrow.fx.Timer<BIOPartialOf<Nothing>>

@JvmName("parApplicative")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun parApplicative(): Applicative<BIOPartialOf<Nothing>> = arrow.fx.BIO
  .concurrent()
  .parApplicative() as arrow.typeclasses.Applicative<BIOPartialOf<Nothing>>

@JvmName("parApplicative")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun parApplicative(ctx: CoroutineContext): Applicative<BIOPartialOf<Nothing>> = arrow.fx.BIO
  .concurrent()
  .parApplicative(ctx) as arrow.typeclasses.Applicative<BIOPartialOf<Nothing>>

@JvmName("fork")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.fork(): Kind<BIOPartialOf<Nothing>, Fiber<BIOPartialOf<Nothing>, A>> = arrow.fx.BIO.concurrent().run {
  this@fork.fork<A>() as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.typeclasses.Fiber<BIOPartialOf<Nothing>, A>>
}

@JvmName("parTraverse")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <G, A, B> Kind<G, A>.parTraverse(
  ctx: CoroutineContext,
  TG: Traverse<G>,
  f: Function1<A, Kind<BIOPartialOf<Nothing>, B>>
): Kind<BIOPartialOf<Nothing>, Kind<G, B>> = arrow.fx.BIO.concurrent().run {
  this@parTraverse.parTraverse<G, A, B>(ctx, TG, f) as arrow.Kind<BIOPartialOf<Nothing>, arrow.Kind<G, B>>
}

@JvmName("parTraverse")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <G, A, B> Kind<G, A>.parTraverse(TG: Traverse<G>, f: Function1<A, Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, Kind<G,
  B>> = arrow.fx.BIO.concurrent().run {
  this@parTraverse.parTraverse<G, A, B>(TG, f) as arrow.Kind<BIOPartialOf<Nothing>, arrow.Kind<G, B>>
}

@JvmName("parTraverse")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Iterable<A>.parTraverse(ctx: CoroutineContext, f: Function1<A, Kind<BIOPartialOf<Nothing>, B>>):
  Kind<BIOPartialOf<Nothing>, List<B>> = arrow.fx.BIO.concurrent().run {
  this@parTraverse.parTraverse<A, B>(ctx, f) as arrow.Kind<BIOPartialOf<Nothing>, kotlin.collections.List<B>>
}

@JvmName("parTraverse")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> Iterable<A>.parTraverse(f: Function1<A, Kind<BIOPartialOf<Nothing>, B>>): Kind<BIOPartialOf<Nothing>, List<B>> =
  arrow.fx.BIO.concurrent().run {
    this@parTraverse.parTraverse<A, B>(f) as arrow.Kind<BIOPartialOf<Nothing>, kotlin.collections.List<B>>
  }

@JvmName("parSequence")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <G, A> Kind<G, Kind<BIOPartialOf<Nothing>, A>>.parSequence(TG: Traverse<G>, ctx: CoroutineContext): Kind<BIOPartialOf<Nothing>, Kind<G,
  A>> = arrow.fx.BIO.concurrent().run {
  this@parSequence.parSequence<G, A>(TG, ctx) as arrow.Kind<BIOPartialOf<Nothing>, arrow.Kind<G, A>>
}

@JvmName("parSequence")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <G, A> Kind<G, Kind<BIOPartialOf<Nothing>, A>>.parSequence(TG: Traverse<G>): Kind<BIOPartialOf<Nothing>, Kind<G, A>> =
  arrow.fx.BIO.concurrent().run {
    this@parSequence.parSequence<G, A>(TG) as arrow.Kind<BIOPartialOf<Nothing>, arrow.Kind<G, A>>
  }

@JvmName("parSequence")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Iterable<Kind<BIOPartialOf<Nothing>, A>>.parSequence(ctx: CoroutineContext): Kind<BIOPartialOf<Nothing>, List<A>> =
  arrow.fx.BIO.concurrent().run {
    this@parSequence.parSequence<A>(ctx) as arrow.Kind<BIOPartialOf<Nothing>, kotlin.collections.List<A>>
  }

@JvmName("parSequence")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Iterable<Kind<BIOPartialOf<Nothing>, A>>.parSequence(): Kind<BIOPartialOf<Nothing>, List<A>> = arrow.fx.BIO.concurrent().run {
  this@parSequence.parSequence<A>() as arrow.Kind<BIOPartialOf<Nothing>, kotlin.collections.List<A>>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  f: Function4<A, B, C, D, E>
): BIO<Nothing, E> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E>(fa, fb, fc, fd, f) as BIO<Nothing, E>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  fe: Kind<BIOPartialOf<Nothing>, E>,
  f: Function5<A, B, C, D, E, G>
): Kind<BIOPartialOf<Nothing>, G> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E, G>(fa, fb, fc, fd, fe, f) as arrow.Kind<BIOPartialOf<Nothing>, G>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  fe: Kind<BIOPartialOf<Nothing>, E>,
  fg: Kind<BIOPartialOf<Nothing>, G>,
  f: Function6<A, B, C, D, E, G, H>
): BIO<Nothing, H> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E, G, H>(fa, fb, fc, fd, fe, fg, f) as BIO<Nothing, H>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H, I> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  fe: Kind<BIOPartialOf<Nothing>, E>,
  fg: Kind<BIOPartialOf<Nothing>, G>,
  fh: Kind<BIOPartialOf<Nothing>, H>,
  f: Function7<A, B, C, D, E, G, H, I>
): Kind<BIOPartialOf<Nothing>, I> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E, G, H, I>(fa, fb, fc, fd, fe, fg, fh, f) as arrow.Kind<BIOPartialOf<Nothing>, I>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H, I, J> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  fe: Kind<BIOPartialOf<Nothing>, E>,
  fg: Kind<BIOPartialOf<Nothing>, G>,
  fh: Kind<BIOPartialOf<Nothing>, H>,
  fi: Kind<BIOPartialOf<Nothing>, I>,
  f: Function8<A, B, C, D, E, G, H, I, J>
): Kind<BIOPartialOf<Nothing>, J> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E, G, H, I, J>(fa, fb, fc, fd, fe, fg, fh, fi, f) as
    arrow.Kind<BIOPartialOf<Nothing>, J>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H, I, J, K> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  fd: Kind<BIOPartialOf<Nothing>, D>,
  fe: Kind<BIOPartialOf<Nothing>, E>,
  fg: Kind<BIOPartialOf<Nothing>, G>,
  fh: Kind<BIOPartialOf<Nothing>, H>,
  fi: Kind<BIOPartialOf<Nothing>, I>,
  fj: Kind<BIOPartialOf<Nothing>, J>,
  f: Function9<A, B, C, D, E, G, H, I, J, K>
): Kind<BIOPartialOf<Nothing>, K> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D, E, G, H, I, J, K>(fa, fb, fc, fd, fe, fg, fh, fi, fj, f) as
    arrow.Kind<BIOPartialOf<Nothing>, K>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B> CoroutineContext.raceN(fa: Kind<BIOPartialOf<Nothing>, A>, fb: Kind<BIOPartialOf<Nothing>, B>): Kind<BIOPartialOf<Nothing>, Either<A, B>> =
  arrow.fx.BIO.concurrent().run {
    this@raceN.raceN<A, B>(fa, fb) as arrow.Kind<BIOPartialOf<Nothing>, arrow.core.Either<A, B>>
  }

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C> CoroutineContext.raceN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>
): Kind<BIOPartialOf<Nothing>, Race3<A, B, C>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C>(fa, fb, fc) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race3<A, B, C>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>
): Kind<BIOPartialOf<Nothing>, Race4<A, B, C, D>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D>(a, b, c, d) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race4<A, B, C, D>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>,
  e: Kind<BIOPartialOf<Nothing>, E>
): Kind<BIOPartialOf<Nothing>, Race5<A, B, C, D, E>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D, E>(a, b, c, d, e) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race5<A, B, C, D, E>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>,
  e: Kind<BIOPartialOf<Nothing>, E>,
  g: Kind<BIOPartialOf<Nothing>, G>
): Kind<BIOPartialOf<Nothing>, Race6<A, B, C, D, E, G>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D, E, G>(a, b, c, d, e, g) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race6<A, B, C, D, E,
    G>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>,
  e: Kind<BIOPartialOf<Nothing>, E>,
  g: Kind<BIOPartialOf<Nothing>, G>,
  h: Kind<BIOPartialOf<Nothing>, H>
): Kind<BIOPartialOf<Nothing>, Race7<A, B, C, D, E, G, H>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D, E, G, H>(a, b, c, d, e, g, h) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race7<A, B, C,
    D, E, G, H>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H, I> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>,
  e: Kind<BIOPartialOf<Nothing>, E>,
  g: Kind<BIOPartialOf<Nothing>, G>,
  h: Kind<BIOPartialOf<Nothing>, H>,
  i: Kind<BIOPartialOf<Nothing>, I>
): Kind<BIOPartialOf<Nothing>, Race8<A, B, C, D, E, G, H, I>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D, E, G, H, I>(a, b, c, d, e, g, h, i) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race8<A,
    B, C, D, E, G, H, I>>
}

@JvmName("raceN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D, E, G, H, I, J> CoroutineContext.raceN(
  a: Kind<BIOPartialOf<Nothing>, A>,
  b: Kind<BIOPartialOf<Nothing>, B>,
  c: Kind<BIOPartialOf<Nothing>, C>,
  d: Kind<BIOPartialOf<Nothing>, D>,
  e: Kind<BIOPartialOf<Nothing>, E>,
  g: Kind<BIOPartialOf<Nothing>, G>,
  h: Kind<BIOPartialOf<Nothing>, H>,
  i: Kind<BIOPartialOf<Nothing>, I>,
  j: Kind<BIOPartialOf<Nothing>, J>
): Kind<BIOPartialOf<Nothing>, Race9<A, B, C, D, E, G, H, I, J>> = arrow.fx.BIO.concurrent().run {
  this@raceN.raceN<A, B, C, D, E, G, H, I, J>(a, b, c, d, e, g, h, i, j) as
    arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Race9<A, B, C, D, E, G, H, I, J>>
}

@JvmName("Promise")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Promise(): Kind<BIOPartialOf<Nothing>, Promise<BIOPartialOf<Nothing>, A>> = arrow.fx.BIO
  .concurrent()
  .Promise<A>() as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Promise<BIOPartialOf<Nothing>, A>>

@JvmName("Semaphore")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun Semaphore(n: Long): Kind<BIOPartialOf<Nothing>, Semaphore<BIOPartialOf<Nothing>>> = arrow.fx.BIO
  .concurrent()
  .Semaphore(n) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.Semaphore<BIOPartialOf<Nothing>>>

@JvmName("MVar")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> MVar(a: A): Kind<BIOPartialOf<Nothing>, MVar<BIOPartialOf<Nothing>, A>> = arrow.fx.BIO
  .concurrent()
  .MVar<A>(a) as arrow.Kind<BIOPartialOf<Nothing>, arrow.fx.MVar<BIOPartialOf<Nothing>, A>>

@JvmName("bindingConcurrent")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <B> bindingConcurrent(c: suspend ConcurrentContinuation<BIOPartialOf<Nothing>, *>.() -> B): Kind<BIOPartialOf<Nothing>, B> =
  arrow.fx.BIO
    .concurrent()
    .bindingConcurrent<B>(c) as arrow.Kind<BIOPartialOf<Nothing>, B>

@JvmName("sleep")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun sleep(duration: Duration): Kind<BIOPartialOf<Nothing>, Unit> = arrow.fx.BIO
  .concurrent()
  .sleep(duration) as arrow.Kind<BIOPartialOf<Nothing>, kotlin.Unit>

@JvmName("waitFor")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.waitFor(duration: Duration, p2_772401952: Kind<BIOPartialOf<Nothing>, A>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO.concurrent().run {
    this@waitFor.waitFor<A>(duration, p2_772401952) as arrow.Kind<BIOPartialOf<Nothing>, A>
  }

@JvmName("waitFor")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> Kind<BIOPartialOf<Nothing>, A>.waitFor(duration: Duration): Kind<BIOPartialOf<Nothing>, A> = arrow.fx.BIO.concurrent().run {
  this@waitFor.waitFor<A>(duration) as arrow.Kind<BIOPartialOf<Nothing>, A>
}

@JvmName("cancelable")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A> cancelable(k: Function1<Function1<Either<Throwable, A>, Unit>, Kind<BIOPartialOf<Nothing>, Unit>>): Kind<BIOPartialOf<Nothing>, A> =
  arrow.fx.BIO
    .concurrent()
    .cancelable<A>(k) as arrow.Kind<BIOPartialOf<Nothing>, A>

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  f: Function2<A, B, C>
): IO<C> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C>(fa, fb, f) as IO<C>
}

@JvmName("parMapN")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun <A, B, C, D> CoroutineContext.parMapN(
  fa: Kind<BIOPartialOf<Nothing>, A>,
  fb: Kind<BIOPartialOf<Nothing>, B>,
  fc: Kind<BIOPartialOf<Nothing>, C>,
  f: Function3<A, B, C, D>
): Kind<BIOPartialOf<Nothing>, D> = arrow.fx.BIO.concurrent().run {
  this@parMapN.parMapN<A, B, C, D>(fa, fb, fc, f) as arrow.Kind<BIOPartialOf<Nothing>, D>
}

@JvmName("dispatchers")
@Suppress(
  "UNCHECKED_CAST",
  "USELESS_CAST",
  "EXTENSION_SHADOWED_BY_MEMBER",
  "UNUSED_PARAMETER"
)
fun dispatchers(): Dispatchers<BIOPartialOf<Nothing>> = arrow.fx.BIO
  .concurrent()
  .dispatchers() as arrow.fx.typeclasses.Dispatchers<BIOPartialOf<Nothing>>

@Suppress(
  "UNCHECKED_CAST",
  "NOTHING_TO_INLINE"
)
inline fun Companion.concurrent(): IODefaultConcurrent = concurrent_singleton
