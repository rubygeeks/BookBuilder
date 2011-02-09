#!/usr/bin/ruby
require 'rubygems'
require 'fileutils'
require 'maruku'


def scan_file(file_name)
    if File.ftype(file_name) == 'file'
      #puts file_name
  elsif File.ftype(file_name) == 'directory'
      Dir[file_name + "/*"].each { |f| scan_file(f) }
  end

end

def file_write(filepath,content)
    File.open(filepath, 'w') {|f| f.write(content) }
end

def convert_html
    
    files = scan_file('input')
    
    files.each do |f|  
        pattern = /.*\/(.*)\.(.*)/
        filename = f.match(pattern)[1] + ".html"
        #puts f
        file = File.open(f, 'r')
        file_content = file.readlines
        
        #if f == 'input/index.md'
        #    puts file_content
        #end
        if f != 'input/index.md'
            FileUtils.touch "output/" + filename
            html_content = Maruku.new(file_content.to_s).to_html
            file_write("output/" + filename, html_content)
            puts "created chapter #{f}"
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
    pattern = /.*\/(.*)\.(.*)/
    FileUtils.touch('input/index.md')
    file = File.open('input/index.md', 'w')
    files.each do |f|
        filename = f.match(pattern)[1] # filename is being used as chapter-name here
        #puts filename
        #if f != "input/index.md"
            file.puts("* [" + filename + "](" + filename + ".html)")
            #index_file =  "input/index.md"
            #puts index_file.readlines
        #end
    end
    #puts files
    
end

input = ARGV.first.to_s

# if input == "html_book"
#     convert_html
# else
#     if input == "start"
#         puts "How many Chapters?"
#         @ans = gets.chomp
#         puts "Edit your chapters inside input/ directory"
#     end
# else
#     puts "Please use a proper command \n html_book -- To create a html book"

case input
when "html_book"
    #convert_html
    #files = scan_file("input")
    #FileUtils.touch('input/index.md')
    #file = File.open('input/index.md', 'w')
    #files.each do |f|
    #    file =  File.new("input/index.md", 'w+')
    #    file.puts(f)
        
    index_file = File.open('input/index.md', 'r').readlines
    html = Maruku.new(index_file.to_s).to_html
    File.open('output/index.html','w') {|f| f.write(html)}

    convert_html
    
when "start"
    puts "How many Chapters?"
    @ans = STDIN.gets.chomp
    create_markdown
    puts "Edit your chapters inside input/ directory"
    create_toc
# when "write"
#     index_file = File.open('input/index.md', 'r').readlines
#     html = Maruku.new(index_file.to_s).to_html
#     File.open('output/index.html','w') {|f| f.write(html)}

when "clean"
    system "rm -rf input/*"
    system "rm -rf output/*"
    puts "Cleaned up! Create your new book now"

else
    puts "Please use a proper command \n start -- To get started \n html_book -- To create a html book"
end

