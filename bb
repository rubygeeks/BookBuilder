#!/usr/bin/ruby
require 'rubygems'
require 'fileutils'
require 'maruku'
require 'erb'

def scan_file(file_name)
    if File.ftype(file_name) == 'file'
   
  elsif File.ftype(file_name) == 'directory'
      Dir[file_name + "/*"].each { |f| scan_file(f) }
  end

end

def file_read(filepath)
  f = File.open(filepath, 'r')
  f.readlines.join("")
end

def file_write(filepath,content)
    File.open(filepath, 'w') {|f| f.write(content) }
end

def convert_html
  
  files = scan_file('input')
  
  files.each do |f|  
    pattern = /.*\/(.*)\.(.*)/
    filename = f.match(pattern)[1] + ".html"
    
    file = File.open(f, 'r')
    file_content = file.readlines
    
    FileUtils.touch "output/" + filename
    html_content = Maruku.new(file_content.to_s).to_html
    page_template = file_read('layouts/chapter.html')
    content = page_template.gsub("{content}",html_content)
    template = ERB.new(content)
    content = template.result(binding)
    
    file_write("output/" + filename, content)
    puts "created chapter #{f}"
    
    if filename != "index.html"
      num = filename.match(/([0-9]+)/)
      num = num[1].to_i
      prevChapter = "chapter-#{num-1}"
      nextChapter = "chapter-#{num+1}"
      puts prevChapter 
      puts nextChapter
      
    end
    
  end  
end

def create_markdown
    chapters = @ans.to_i

    chapters.times do |c|
        if c < 9
        FileUtils.cp('templates/chapter.md', "input/chapter-0#{c+1}.md")
        else
            FileUtils.cp('templates/chapter.md', "input/chapter-#{c+1}.md")
        end
    end
end

def create_toc
    files = scan_file("input").sort!
    pattern = /.*\/(.*)\.(.*)/ # pattern to extract the filename alone, leaving path and the ext. (.html or .md)
    FileUtils.touch('input/index.md')
    file = File.open('input/index.md', 'w')
    files.each do |f|
        filename = f.match(pattern)[1] # filename is being used as chapter-name here
      
        if f != "input/index.md"
            file.puts("* [" + filename + "](" + filename + ".html)")
        end
    end
    
end

# The terminal interaction goes here

input = ARGV.first.to_s

case input
when "html_book"
 
    convert_html
    
when "start"
 
  puts "How many Chapters?"
  @ans = STDIN.gets.chomp
  create_markdown
  puts "Edit your chapters inside input/ directory"
  create_toc
 
when "clean"
    system "rm -rf input/*"
    system "rm -rf output/*"
    puts "Cleaned up! Create your new book now"

else
    puts "Please use a proper command \n start -- To get started \n html_book -- To create a html book"
end

