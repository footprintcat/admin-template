<template>
    <div v-if="editInfoRef">
        <!-- <p>editInfo: {{ editInfoRef }}</p> -->
        <!-- <p>url: {{ url }}</p> -->

        <div style="display: grid; grid-template-columns: 100px 1fr;">
            <el-image :src="url" :style="{ height: `${height}px`, width: `${ratio * height}px` }">
                <template #error>
                    <div class="image-slot">
                        <el-icon>
                            <IconPicture />
                        </el-icon>
                    </div>
                </template>
            </el-image>
            <div>
                <p style="margin: 0; padding: 0;">
                    <el-text>
                        颜色：{{ editInfoRef.custom.color || '默认' }}
                    </el-text>
                    <el-color-picker v-model="editInfoRef.custom.color" size="small"
                        style="margin-left: 5px; margin-right: 5px;" />
                    <el-button size="small" style="margin-top: -5px;"
                        @click="() => { if (editInfoRef) { delete editInfoRef.custom.color } }">
                        恢复默认
                    </el-button>
                </p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { Picture as IconPicture } from '@element-plus/icons-vue'
import { getIconUrl } from '@/utils/device_icon_utils'

const emit = defineEmits(['update:editInfo'])

interface EditInfo {
    info: {
        icon: string;
        variant: string;
    };
    custom: {
        color?: string;
    };
}

const props = defineProps<{
    editInfo: string;
}>()

const editInfoRef = ref<EditInfo>()

// const url = ref<string>('')
const url = computed((): string => editInfoRef.value ? getIconUrl(editInfoRef.value.info.icon, editInfoRef.value.info.variant, editInfoRef.value.custom.color) : '')

onMounted(() => {
    if (props.editInfo) {
        editInfoRef.value = JSON.parse(props.editInfo)
    } else {
        editInfoRef.value = undefined
    }
})

// width : height = ratio   =>   width = ratio * height
const ratio = 150 / 180
const height = 100

watch(() => editInfoRef.value, (newVal) => {
    emit('update:editInfo', JSON.stringify(newVal))
}, {
    deep: true,
})

</script>

<style scoped>
.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
}

.image-slot .el-icon {
    font-size: 24px;
}
</style>
