#!/usr/bin/env ruby

require 'rubygems'
require 'fileutils'
require 'yaml'
require 'erb'



# Get configuration from <source>/_config.yml

config_file = File.join('config.yml')
begin
  @config = YAML.load_file(config_file)
  raise "Invalid configuration - #{config_file}" if !config.is_a?(Hash)
  $stdout.puts "Configuration from #{config_file}"
rescue => err
  $stderr.puts "WARNING: Could not read configuration. " +
    "Using defaults (and options)."
  $stderr.puts "\t" + err.to_s
  config = {}
end

def file_write(filepath,content)
  File.open(filepath, 'w') {|f| f.write(content) }
end

def file_read(filepath)
  f = File.open(filepath, 'r')
  f.readlines.join("")
end


command = ARGV.join(" ")

if command == "create_book"
  puts "Creating book.."
end

if command == "create_pages"

  puts "Make sure you've edited the file config.yaml"
  puts "Creating pages.."
  chapters = @config['chapters']
  chapters = chapters.to_i
  FileUtils.mkdir('chapters')
  for i in 0..chapters
    FileUtils.cp('templates/page.markdown', "chapters/#{i}.markdown")
  end
  puts "Done!"
  
end
