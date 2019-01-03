package io.radanalytics.operator.common;

import io.fabric8.kubernetes.api.model.apiextensions.CustomResourceDefinition;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.radanalytics.operator.common.crd.InfoClass;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CustomResourceWatcher<T extends EntityInfo> extends AbstractWatcher<T> {

    // use via builder
    private CustomResourceWatcher(String namespace,
                                  String entityName,
                                  KubernetesClient client,
                                  CustomResourceDefinition crd,
                                  BiConsumer<T, String> onAdd,
                                  BiConsumer<T, String> onDelete,
                                  BiConsumer<T, String> onModify,
                                  Function<InfoClass, T> convert) {
        super(true, namespace, entityName, client, crd, null, onAdd, onDelete, onModify, null, null, convert);
    }

    public static class Builder<T> {
        private String namespace = "*";
        private String entityName;
        private KubernetesClient client;
        private CustomResourceDefinition crd;

        private BiConsumer<T, String> onAdd;
        private BiConsumer<T, String> onDelete;
        private BiConsumer<T, String> onModify;
        private Function<InfoClass, T> convert;

        public Builder<T> withNamespace(String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Builder<T> withEntityName(String entityName) {
            this.entityName = entityName;
            return this;
        }

        public Builder<T> withClient(KubernetesClient client) {
            this.client = client;
            return this;
        }

        public Builder<T> withCrd(CustomResourceDefinition crd) {
            this.crd = crd;
            return this;
        }

        public Builder<T> withOnAdd(BiConsumer<T, String> onAdd) {
            this.onAdd = onAdd;
            return this;
        }

        public Builder<T> withOnDelete(BiConsumer<T, String> onDelete) {
            this.onDelete = onDelete;
            return this;
        }

        public Builder<T> withOnModify(BiConsumer<T, String> onModify) {
            this.onModify = onModify;
            return this;
        }

        public Builder<T> withConvert(Function<InfoClass, T> convert) {
            this.convert = convert;
            return this;
        }

        public CustomResourceWatcher build() {
            return new CustomResourceWatcher(namespace, entityName, client, crd, onAdd, onDelete, onModify, convert);
        }
    }

    @Override
    public CompletableFuture<CustomResourceWatcher<T>> watch() {
        return createCustomResourceWatch().thenApply(watch -> this);
    }
}

