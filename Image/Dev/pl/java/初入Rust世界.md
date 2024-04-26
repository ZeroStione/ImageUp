# Rust入门

<img src="https://zerostione.github.io/ImageUp//Image/NewBlog/Rust/p7.jpg" style="zoom: 33%;" />

---

:bookmark_tabs: 主要参考:

- [The Rust Programming Language (TRPL)](https://nostarch.com/rust-programming-language-2nd-edition)

- [Rust 程序设计语言](https://github.com/KaiserY/trpl-zh-cn)

- [Rust 语言中文社区](https://rust.cc/)

<img src="https://zerostione.github.io/ImageUp//Image/NewBlog/Rust/p4.jpg" style="zoom: 33%;" />

---

## 安装

:waning_crescent_moon: **Windows** 

- [Install Rust](https://www.rust-lang.org/tools/install)

- 更换下载镜像源

  <p style="font-family: 'Roboto Slab'; font-size: 15px;">
  $ENV:RUSTUP_DIST_SERVER='https://mirrors.ustc.edu.cn/rust-static'
  $ENV:RUSTUP_UPDATE_ROOT='https://mirrors.ustc.edu.cn/rust-static/rustup'
  </p>

- rustup-init.exe

  <div style="font-family: 'Roboto Slab'; font-size: 15px;color:#ff8800;">
  > rustc --version
  </br>    
  rustc 1.71.1 (eb26296b5 2023-08-03)
  </div>

---

```bash
rustup update         #更新Rust
rustup self uninstall #卸载Rust
```

----

<img src="https://zerostione.github.io/ImageUp//Image/NewBlog/Rust/p1.png" style="zoom: 25%;" />

## Hello Rust





---

## Framework

1.**[Yew](https://yew.rs/): Rust Web应用框架**

> :roll_eyes: *不得不说一下 Yew 官方中文文档已经停止维护了，中文文档的示例版本由于过于落后目前存在兼容问题，有时候有些事情做不好就别做，容易1+1<1。*

