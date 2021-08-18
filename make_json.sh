if [ ! -d fluorite-7 ]
then
	mkdir -p fluorite-7
	(
		cd fluorite-7
		curl https://raw.githubusercontent.com/MirrgieRiana/fluorite-7/master/install.bash | bash
	)
fi

bash fluorite-7/fluorite7 src/fl7/make_json.fl7
