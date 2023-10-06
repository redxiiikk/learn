use std::io::stdin;

fn main() {
    // read input from stdin
    println!("Enter your name: ");
    let mut input = String::new();
    
    stdin().read_line(&mut input).unwrap();
    
    println!("Hello, world! {input}");
}
