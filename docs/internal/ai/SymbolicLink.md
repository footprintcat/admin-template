## 创建符号链接并提交到 Git 仓库

## Windows 下创建符号链接 (管理员权限)

cmd 命令行可以创建相对链接的符号链接

```batch
E:
cd E:\Project\admin-template
:: 当前使用 ↓
:: trae
mkdir -p .trae\rules
mklink /D ".trae\rules\project"             "..\..\.ai\rules\project"
mklink    ".trae\rules\project_rules.md"    "..\..\.ai\rules\project_rules.md"
mklink /D ".trae\skills"                    "..\..\.ai\skills"
:: qoder
mkdir -p .qoder\rules
mklink /D ".qoder\rules\project"            "..\..\.ai\rules\project"
mklink    ".qoder\rules\project_rules.md"   "..\..\.ai\rules\project_rules.md"
mklink /D ".qoder\skills"                   "..\..\.ai\skills"

:: 这种方式创建后提交还是会提交完整文件到 Git 仓库中
:: mklink /H ".qoder\rules\intro.md" ".ai\rules\intro.md"
```

PowerShell 下无法创建相对路径的符号链接

```ps1
mkdir -p .qoder\rules
New-Item -ItemType SymbolicLink -Path ".qoder\rules\intro.md" -Target ".ai\rules\intro.md"
Get-Item ".qoder\rules\intro.md" | Select-Object Target, LinkType

# 删除现有的符号链接
Remove-Item ".qoder\rules\intro.md"
```

## linux / macOS 下创建符号链接

```sh
# 需测试
mkdir -p .qoder/rules
ln -s ./.ai/rules/intro.md .qoder/rules/intro.md
```
