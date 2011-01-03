#!/usr/bin/env ruby

require 'rubygems'
require 'fileutils'
require 'yaml'
require 'erb'

# Get configuration from <source>/_config.yml
config_file = File.join('config.yml')
begin
  config = YAML.load_file(config_file)
  raise "Invalid configuration - #{config_file}" if !config.is_a?(Hash)
  $stdout.puts "Configuration from #{config_file}"
rescue => err
  $stderr.puts "WARNING: Could not read configuration. " +
    "Using defaults (and options)."
  $stderr.puts "\t" + err.to_s
  config = {}
end

command = ARGV.join(" ")

if command == "create_book"
  puts "Creating book.."
end
