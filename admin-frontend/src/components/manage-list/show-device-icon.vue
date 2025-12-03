<template>
    <!-- {{ url }} -->
    <!-- {{ props.editInfo }} -->
    <el-image v-if="editInfo" :src="url" :style="{ width: `${props.height * ratio}px`, height: `${props.height}px` }">
        <template #error>
            <div class="image-slot">
                <el-icon>
                    <IconPicture />
                </el-icon>
            </div>
        </template>
    </el-image>
</template>

<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { Picture as IconPicture } from '@element-plus/icons-vue'
import { getIconUrl } from '@/utils/device_icon_utils'

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
    height: number;
}>()

const url = ref<string>('')

const ratio = 150 / 180

function updateEditInfo(editInfoStr: string) {
    const editInfo: EditInfo = JSON.parse(props.editInfo)
    // console.log('editInfo.custom.color', editInfo, editInfo.custom.color)
    url.value = getIconUrl(editInfo.info.icon, editInfo.info.variant, editInfo.custom.color)

}
onMounted(() => {
    if (props.editInfo) {
        updateEditInfo(props.editInfo)
    }
})

watch(() => props.editInfo, (newVal) => {
    updateEditInfo(newVal)
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
    font-size: 16px;
}
</style>
