package com.github.redxiiikk.learn.gatewayDispatch.loadbalancer.isolation

import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.loadbalancer.Request
import org.springframework.cloud.client.loadbalancer.RequestDataContext
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier
import reactor.core.publisher.Flux

class IsolationServiceInstanceListSupplier(
    delegate: ServiceInstanceListSupplier,
    private val property: IsolationConfigProperty
) : DelegatingServiceInstanceListSupplier(delegate) {

    override fun get(): Flux<MutableList<ServiceInstance>> {
        return if (property.enable) {
            return delegate.get().map { selectServiceInstancesByHint(it, property.baselineEnvName) }
        } else {
            delegate.get()
        }
    }

    override fun get(request: Request<*>): Flux<MutableList<ServiceInstance>> {
        return if (property.enable) {
            delegate.get(request).map { filterByIsolation(request, it) }
        } else {
            delegate.get(request)
        }
    }


    private fun filterByIsolation(
        request: Request<*>,
        instances: MutableList<ServiceInstance>
    ): MutableList<ServiceInstance> {
        val env: String = when (val context = request.context) {
            is RequestDataContext -> {
                context.clientRequest.headers.getFirst(property.isolationHeaderKey) ?: property.baselineEnvName
            }

            else -> return selectServiceInstancesByHint(instances, property.baselineEnvName)
        }

        return selectServiceInstancesByHint(instances, env).ifEmpty {
            selectServiceInstancesByHint(instances, property.baselineEnvName)
        }
    }

    private fun selectServiceInstancesByHint(
        instances: MutableList<ServiceInstance>,
        hint: String
    ): MutableList<ServiceInstance> {
        return instances.filter { hint == it.metadata[property.isolationMetadataKey] }.toMutableList()
    }
}