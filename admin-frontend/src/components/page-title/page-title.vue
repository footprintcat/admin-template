<template>
  <div class="page-title-container">
    <el-icon class="title-icon" :class="[props.type]" :size="props.type === 'normal' ? 48 : 28" v-if="hasIcon">
      <slot name="title-icon" v-if="hasNamedSlot"></slot>
      <slot v-else-if="hasDefaultSlot"></slot>
    </el-icon>
    <h1 class="title-text" :class="[props.type]">{{ props.title }}</h1>
  </div>
</template>

<script setup lang="ts">
interface Props {
  title: string
  // 建议 normal 风格下使用线条风格icon, single-line 风格下使用填充风格icon
  type?: 'normal' | 'single-line'
}

const props = withDefaults(defineProps<Props>(), {
  type: 'normal',
})

const slots = useSlots()
const hasNamedSlot = computed(() => !!slots['title-icon'])
const hasDefaultSlot = computed(() => !!slots.default)
const hasIcon = computed(() => hasNamedSlot.value || hasDefaultSlot.value)
</script>

<style scoped>
.page-title-container {
  text-align: center;
  margin-bottom: 15px;
}

.title-text {
  font-size: 24px;
  font-weight: bold;
  font-family: initial;
}

.title-icon.single-line {
  vertical-align: middle;
  margin-right: 3px;
  margin-top: 3px;
}

.title-text.single-line {
  vertical-align: middle;
  display: inline;
}
</style>
