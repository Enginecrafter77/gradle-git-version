package dev.enginecrafter77.gitversion;

import org.gradle.api.Transformer;
import org.gradle.api.provider.Provider;
import org.gradle.api.specs.Spec;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

public abstract class ProviderDelegate<T> implements Provider<T> {
	public abstract Provider<T> getDelegatedProvider();

	@Nonnull
	@Override
	public T get()
	{
		return this.getDelegatedProvider().get();
	}

	@Nullable
	@Override
	public T getOrNull()
	{
		return this.getDelegatedProvider().getOrNull();
	}

	@Nonnull
	@Override
	public T getOrElse(@Nonnull T defaultValue)
	{
		return this.getDelegatedProvider().getOrElse(defaultValue);
	}

	@Nonnull
	@Override
	public <S> Provider<S> map(@Nonnull Transformer<? extends S, ? super T> transformer)
	{
		return this.getDelegatedProvider().map(transformer);
	}

	@Nonnull
	@Override
	@SuppressWarnings("UnstableApiUsage")
	public Provider<T> filter(@Nonnull Spec<? super T> spec)
	{
		return this.getDelegatedProvider().filter(spec);
	}

	@Nonnull
	@Override
	public <S> Provider<S> flatMap(@Nonnull Transformer<? extends Provider<? extends S>, ? super T> transformer)
	{
		return this.getDelegatedProvider().flatMap(transformer);
	}

	@Override
	public boolean isPresent()
	{
		return this.getDelegatedProvider().isPresent();
	}

	@Nonnull
	@Override
	public Provider<T> orElse(@Nonnull T value)
	{
		return this.getDelegatedProvider().orElse(value);
	}

	@Nonnull
	@Override
	public Provider<T> orElse(@Nonnull Provider<? extends T> provider)
	{
		return this.getDelegatedProvider().orElse(provider);
	}

	@Override
	@Nonnull
	@Deprecated
	public Provider<T> forUseAtConfigurationTime()
	{
		return this.getDelegatedProvider().forUseAtConfigurationTime();
	}

	@Nonnull
	@Override
	public <U, R> Provider<R> zip(@Nonnull Provider<U> right, @Nonnull BiFunction<? super T, ? super U, ? extends R> combiner)
	{
		return this.getDelegatedProvider().zip(right, combiner);
	}
}
